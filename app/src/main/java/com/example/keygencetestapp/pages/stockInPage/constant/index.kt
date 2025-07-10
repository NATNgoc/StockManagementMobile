package com.example.keygencetestapp.pages.stockInPage.constant

import com.example.keygencetestapp.R
import com.example.keygencetestapp.pages.stockInPage.types.ImportOptionType


object ImportOptionsConst {
    val excelOption: ImportOptionType = ImportOptionType(
        1,
        "Excel File",
        "Import via excel with the pre-defined format",
        iconResId = R.drawable.import_option_excel
    )
    val ocrOption: ImportOptionType = ImportOptionType(
        2,
        "OCR Scanner",
        "Scan Item code by OCR to import",
        iconResId = R.drawable.import_option_ocr
    )
    val qrOption: ImportOptionType = ImportOptionType(
        3,
        "QR Scanner",
        "Scan Item code by QR to import",
        iconResId = R.drawable.qr_scan_icon

    )
}