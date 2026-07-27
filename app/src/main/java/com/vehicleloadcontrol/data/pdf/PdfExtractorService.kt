package com.vehicleloadcontrol.data.pdf

import android.content.Context
import android.net.Uri
import com.vehicleloadcontrol.domain.model.ShippingDocument
import com.vehicleloadcontrol.domain.model.Vehicle
import com.tom_roush.pdfbox.pdfparser.PDFParser
import com.tom_roush.pdfbox.pdfdocument.PDFDocument
import com.tom_roush.pdfbox.pdfpage.PDFPage
import com.tom_roush.pdfbox.pdfextractor.PDFTextStripper
import java.io.InputStream

interface PdfExtractorService {
    suspend fun extractPdfData(context: Context, uri: Uri): Pair<ShippingDocument?, List<Vehicle>>
}

class PdfExtractorServiceImpl : PdfExtractorService {
    override suspend fun extractPdfData(
        context: Context,
        uri: Uri
    ): Pair<ShippingDocument?, List<Vehicle>> {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                val pdfText = extractTextFromPdf(stream)
                val shippingDoc = parseShippingDocument(pdfText)
                val vehicles = parseVehicles(pdfText, shippingDoc?.id ?: 0)
                Pair(shippingDoc, vehicles)
            } ?: Pair(null, emptyList())
        } catch (e: Exception) {
            e.printStackTrace()
            Pair(null, emptyList())
        }
    }

    private fun extractTextFromPdf(inputStream: InputStream): String {
        return try {
            val parser = PDFParser(inputStream)
            val document = PDFDocument(parser)
            val stripper = PDFTextStripper()
            val text = stripper.getText(document)
            document.close()
            text
        } catch (e: Exception) {
            ""
        }
    }

    private fun parseShippingDocument(pdfText: String): ShippingDocument? {
        return try {
            val blNumber = extractBlNumber(pdfText)
            val carrier = extractCarrier(pdfText)
            val shipName = extractShipName(pdfText)
            val shipDate = extractShipDate(pdfText)
            val portOfOrigin = extractPortOfOrigin(pdfText)
            val portOfDestination = extractPortOfDestination(pdfText)

            ShippingDocument(
                blNumber = blNumber,
                carrier = carrier,
                shipName = shipName,
                shipDate = shipDate,
                portOfOrigin = portOfOrigin,
                portOfDestination = portOfDestination,
                pdfPath = "",
                vehicleCount = 0,
                status = "PENDING"
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseVehicles(pdfText: String, shipmentId: Long): List<Vehicle> {
        val vehicles = mutableListOf<Vehicle>()
        // TODO: Implement vehicle parsing logic based on PDF format
        // This is a placeholder that shows the structure
        return vehicles
    }

    private fun extractBlNumber(text: String): String {
        // Pattern: BL or B/L followed by numbers
        val regex = Regex("(?:BL|B/L)[\\s]?([0-9]{10,12})")
        return regex.find(text)?.groupValues?.get(1) ?: ""
    }

    private fun extractCarrier(text: String): String {
        return when {
            text.contains("CMA CGM", ignoreCase = true) -> "CMA CGM"
            text.contains("ZIM", ignoreCase = true) -> "ZIM"
            text.contains("MAERSK", ignoreCase = true) -> "Maersk"
            else -> "OTHER"
        }
    }

    private fun extractShipName(text: String): String {
        // TODO: Implement ship name extraction
        return ""
    }

    private fun extractShipDate(text: String): String {
        // TODO: Implement ship date extraction
        return ""
    }

    private fun extractPortOfOrigin(text: String): String {
        // TODO: Implement port of origin extraction
        return ""
    }

    private fun extractPortOfDestination(text: String): String {
        // TODO: Implement port of destination extraction
        return ""
    }
}
