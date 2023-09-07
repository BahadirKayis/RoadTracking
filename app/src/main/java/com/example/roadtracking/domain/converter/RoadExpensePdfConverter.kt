package com.example.roadtracking.domain.converter

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.example.roadtracking.common.extensions.longToDateConvert
import com.example.roadtracking.data.model.RoadUI
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import javax.inject.Inject

class RoadExpensePdfConverter @Inject constructor(private val context: Context) {
    private val paint = Paint().apply {
        textSize = TITLE_TEXT_SIZE
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }

    private val pageInfo =
        PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, PAGE_NUMBER).create()

    fun createPdf(road: List<RoadUI>, month: Int, title: String = "Yol Masrafı"): File {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val formattedMonth = month.toString().padStart(2, '0')
        val pdfName = "-$formattedMonth-$year.pdf"

        val pdfDocument = PdfDocument()
        val fontMetrics = paint.fontMetrics
        val lineHeight = fontMetrics.descent - fontMetrics.ascent
        val x = pageInfo.pageWidth / 2f // Metnin ortalanacağı yatay pozisyon
        var y = START_VERTICAL_POSITION // İlk dikey pozisyon
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        canvas.drawText(title, x, y, paint) // Başlığı PDF sayfasına çizin

        // Content
        paint.apply {
            textSize = CONTENT_TEXT_SIZE; isFakeBoldText = false; textAlign = Paint.Align.LEFT
        }
        y += lineHeight
        for (item in road) {
            val date = item.dateInMillis.longToDateConvert()
            val company = item.company
            canvas.drawText("$date - $company", COLUMN_X, y, paint)
            y += lineHeight // Yeni bir satıra geçmek için y koordinatını güncelleyin
            if (y + lineHeight >= PAGE_HEIGHT) {
                pdfDocument.finishPage(page) // Mevcut sayfayı tamamlayın
                page = pdfDocument.startPage(pageInfo) // Yeni bir sayfa başlatın
                canvas = page.canvas // Yeni sayfanın canvas'ını alın
                y = 50f // Y koordinatını sıfırlayın veya istediğiniz değere ayarlayın
            }
        }
        pdfDocument.finishPage(page)
        val file = File(context.getExternalFilesDir(null), pdfName)
        pdfDocument.writeTo(FileOutputStream(file))
        pdfDocument.close()

        return file
    }

    companion object {
        const val PAGE_WIDTH = 500
        const val PAGE_HEIGHT = 1000
        const val PAGE_NUMBER = 1
        const val START_VERTICAL_POSITION = 100f
        const val COLUMN_X = 50f
        const val TITLE_TEXT_SIZE = 40f
        const val CONTENT_TEXT_SIZE = 16f
    }
}
