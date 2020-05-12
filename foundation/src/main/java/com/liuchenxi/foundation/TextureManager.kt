package com.liuchenxi.foundation

import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco

class TextureManager{
    companion object{
        fun InitFresco(mContext:Context){
            Fresco.initialize(mContext)
        }
    }
}