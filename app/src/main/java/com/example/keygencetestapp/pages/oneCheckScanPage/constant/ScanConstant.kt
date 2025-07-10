package com.example.keygencetestapp.pages.oneCheckScanPage.constant


object ScanConstant {

    abstract class FeedbackParams(
        open val tone: Int,
        open val onPeriod: Int,
        open val offPeriod: Int,
        open val repeatCount: Int
    )
    data class ErrorFeedbackParams(
        override val tone: Int = 16, // Highest tone [7]
        override val onPeriod: Int = 100, // Short on period [7]
        override val offPeriod: Int = 50, // Short off period [7]
        override val repeatCount: Int = 3 // Repeat a few times [7]
    ) : FeedbackParams(tone, onPeriod, offPeriod, repeatCount)

    data class SuccessFeedbackParams(
        override val tone: Int = 16, // Highest tone [7]
        override val onPeriod: Int = 100, // Short on period [7]
        override val offPeriod: Int = 0, // Short off period [7]
        override val repeatCount: Int = 1 // Repeat a few times [7]
    ) : FeedbackParams(tone, onPeriod, offPeriod, repeatCount)
}