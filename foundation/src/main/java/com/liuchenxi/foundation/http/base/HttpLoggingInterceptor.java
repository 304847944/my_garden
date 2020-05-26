package com.liuchenxi.foundation.http.base;

import com.orhanobut.logger.Logger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 网络请求日志
 *
 * @author zhanghongxing
 * @data 2017/11/23.
 */
public final class HttpLoggingInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");
    private final HttpLoggingInterceptor.Logger logger;
    private volatile HttpLoggingInterceptor.Level level;

    public HttpLoggingInterceptor() {
        this(HttpLoggingInterceptor.Logger.LOGUTIL);
    }

    public HttpLoggingInterceptor(HttpLoggingInterceptor.Logger logger) {
        this.level = HttpLoggingInterceptor.Level.NONE;
        this.logger = logger;
    }

    public HttpLoggingInterceptor setLevel(HttpLoggingInterceptor.Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        } else {
            this.level = level;
            return this;
        }
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return this.level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor.Level level = this.level;
        Request request = chain.request();

        if (level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        } else {
            boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
            boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            Connection connection = chain.connection();
            Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;

            NetworkLogContext ctx = new NetworkLogContext();
            ctx.setMethod(request.method());
            ctx.setUrl(request.url().toString());
            ctx.setProtocol(protocol.toString());

            if (!logHeaders && hasRequestBody) {
                ctx.setExtraMessage( " (" + requestBody.contentLength() + "-byte body)");
            }

            if (logHeaders) {
                ctx.setRequestHeader(request.headers().toMultimap());


                if (logBody && hasRequestBody) {
                    if (this.bodyEncoded(request.headers())) {
                        ctx.setExtraMessage(" (encoded body omitted)");
                    } else {
                        Buffer var29 = new Buffer();
                        requestBody.writeTo(var29);
                        Charset var30 = UTF8;
                        MediaType var32 = requestBody.contentType();
                        if (var32 != null) {
                            var30 = var32.charset(UTF8);
                        }

                        if (isPlaintext(var29)) {
                            ctx.setRequestBody( var29.readString(var30));
                            ctx.setExtraMessage(" (" + requestBody.contentLength() + "-byte body)");
                        } else {
                            ctx.setExtraMessage(" (binary" + requestBody.contentLength() + "-byte body omitted))");
                        }
                    }
                }
            }

            long var28 = System.nanoTime();

            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception exception) {
                ctx.setStatusCode(-1);
                ctx.setMessage(exception.getMessage());
                logger.log(ctx);
                throw exception;
            }

            long var33 = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - var28);
            ctx.setStatusCode(response.code());
            ctx.setMessage(response.message());
            ctx.setRequestTime(var33);

            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            ctx.setResponseBodySize(responseBody.contentLength());

            if (logHeaders) {
                ctx.setResponseHeader(response.headers().toMultimap());

                if (logBody && HttpHeaders.hasBody(response)) {
                    if (this.bodyEncoded(response.headers())) {
                        ctx.setExtraResponseMessage(" (encoded body omitted)");
                    } else {
                        BufferedSource var34 = responseBody.source();
                        var34.request(9223372036854775807L);
                        Buffer var35 = var34.buffer();
                        Charset charset = UTF8;
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            try {
                                charset = contentType.charset(UTF8);
                            } catch (UnsupportedCharsetException var26) {
                                ctx.setExtraResponseMessage("Couldn\'t decode the response body; charset is likely malformed.");
                                logger.log(ctx);
                                return response;
                            }
                        }

                        if (!isPlaintext(var35)) {
                            ctx.setExtraResponseMessage("(binary " + var35.size() + "-byte body omitted)");
                            logger.log(ctx);
                            return response;
                        }

                        if (contentLength != 0L) {
                            String str = var35.clone().readString(charset);
                            str = str.replace("\n", "");
                            ctx.setResponseBody(str);
                        }
                        ctx.setExtraResponseMessage(" (" + var35.size() + "-byte body)");
                    }
                }
            }

            logger.log(ctx);
            return response;
        }
    }

    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer e = new Buffer();
            long byteCount = buffer.size() < 64L ? buffer.size() : 64L;
            buffer.copyTo(e, 0L, byteCount);

            for (int i = 0; i < 16 && !e.exhausted(); ++i) {
                int codePoint = e.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !"identity".equalsIgnoreCase(contentEncoding);
    }

    public static void network(NetworkLogContext ctx) {
        String message = ctx.toConsoleLogString();
            com.orhanobut.logger.Logger.log(com.orhanobut.logger.Logger.INFO, "NET_WORK", message, null);
//            logAdapter.network(ctx);//添加到数据库

    }

    public interface Logger {
        HttpLoggingInterceptor.Logger DEFAULT = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(NetworkLogContext ctx) {
                Platform.get().log(4, ctx.toConsoleLogString(), (Throwable) null);
            }
        };

        HttpLoggingInterceptor.Logger LOGUTIL = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(NetworkLogContext ctx) {
                network(ctx);
            }
        };

        void log(NetworkLogContext ctx);
    }

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY;

        private Level() {
        }
    }

}
