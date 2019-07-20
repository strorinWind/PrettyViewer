package ru.strorin.xlsviewer.data

data class StudentDTO(
    val num: Int,
    val regNum: Int = 0,
    val name: String = "",
    val place: String = "",
    val scores: Int = 0,
    val hostelExam: Boolean = false,
    val hostel: Boolean = false,
    val original: Boolean = false) {

    class Builder {
        private var num: Int = 0
        private var regNum: Int = 0
        private var name: String = ""
        private var place: String = ""
        private var scores: Int = 0
        private var hostelExam: Boolean = false
        private var hostel: Boolean = false
        private var original: Boolean = false

        fun num(num: Int) = apply { this.num = num }
        fun regNum(regNum: Int) = apply { this.regNum = regNum }
        fun name(name: String) = apply { this.name = name }
        fun place(place: String) = apply { this.place = place }
        fun scores(scores: Int) = apply { this.scores = scores }
        fun hostelExam(hostel: Boolean) = apply { this.hostelExam = hostel }
        fun hostel(hostel: Boolean) = apply { this.hostel = hostel }
        fun original(original: Boolean) = apply { this.original }

        fun build() = StudentDTO(
            num,
            regNum,
            name,
            place,
            scores,
            hostelExam,
            hostel,
            original
        )
    }
}