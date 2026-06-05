package com.stefick.scanner.barcode

import android.graphics.Bitmap
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(private val onBarcodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        val bitmap = imageProxy.toBitmap()

        val shortestDimension = minOf(bitmap.width, bitmap.height)
        val cropSize = (shortestDimension * 0.7f).toInt()

        val left = (bitmap.width - cropSize) / 2
        val top = (bitmap.height - cropSize) / 2

        val croppedBitmap = Bitmap.createBitmap(
            bitmap,
            left,
            top,
            cropSize,
            cropSize
        )

        val image = InputImage.fromBitmap(
            croppedBitmap,
            imageProxy.imageInfo.rotationDegrees
        )
        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let { onBarcodeDetected(it) }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}