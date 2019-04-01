package com.enation.javashop.android.lib.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import io.reactivex.Observable

/**
 * Created by LDD on 2018/11/9.
 */
class EcodeHelper {


    fun rxCreateQRImage(content: String, heightPix: Int, logoBm: Bitmap?) :Observable<Bitmap>{
        return Observable.create { observableEmitter ->
            observableEmitter.onNext(createQRImage(content,heightPix,logoBm)!!)
        }
    }

    fun createQRImage(content: String, heightPix: Int, logoBm: Bitmap?): Bitmap? {
        try {
            // if (content == null || "".equals(content)) {
            // return false;
            // }

            //配置参数
            val hints = HashMap<EncodeHintType, Any>()
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
            //容错级别
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
            //设置空白边距的宽度
            // hints.put(EncodeHintType.MARGIN, 2); //default is 4

            // 图像数据转换，使用了矩阵转换
            val bitMatrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, heightPix, heightPix, hints)
            val pixels = IntArray(heightPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until heightPix) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * heightPix + x] = -0x1000000
                    } else {
                        pixels[y * heightPix + x] = -0x1
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap: Bitmap? = Bitmap.createBitmap(heightPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap!!.setPixels(pixels, 0, heightPix, 0, 0, heightPix, heightPix)

            if (logoBm != null) {
                bitmap = addLogo(bitmap, logoBm)
            }

            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 在二维码中间添加Logo图案
     */
    private fun addLogo(src: Bitmap?, logo: Bitmap?): Bitmap? {
        if (src == null) {
            return null
        }

        if (logo == null) {
            return src
        }

        //获取图片的宽高
        val srcWidth = src.width
        val srcHeight = src.height
        val logoWidth = logo.width
        val logoHeight = logo.height

        if (srcWidth == 0 || srcHeight == 0) {
            return null
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src
        }

        //logo大小为二维码整体大小的1/5
        val scaleFactor = srcWidth * 1.0f / 5f / logoWidth.toFloat()
        var bitmap: Bitmap? = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888)
        try {
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(src, 0f, 0f, null)
            canvas.scale(scaleFactor, scaleFactor, (srcWidth / 2).toFloat(), (srcHeight / 2).toFloat())
            canvas.drawBitmap(logo, ((srcWidth - logoWidth) / 2).toFloat(), ((srcHeight - logoHeight) / 2).toFloat(), null)
            canvas.save()
            canvas.restore()
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }

        return bitmap
    }
}