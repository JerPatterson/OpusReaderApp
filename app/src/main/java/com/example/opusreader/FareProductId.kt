package com.example.opusreader

enum class FareProductId(val ID: UInt) {
    OPUS_8TICKETS_STL(32u),
    OPUS_MONTHLY_STL(16u),

    OPUS_2TICKETS_STM(240u),
    OPUS_10TICKETS_STM(440u),

    OPUS_10TICKETS_BUS(728u),

    OPUS_MONTHLY_ALL_MODES_AB(752u),

    OPUS_EVENING_UNLIMITED(744u),


    OCC_2TICKETS_BUS(3316801u),
    OCC_10TICKETS_BUS(3316865u),

    OCC_2TICKETS_ALL_MODES_A(3314625u),

    OCC_1TICKET_ALL_MODES_AB(3314689u),
    OCC_2TICKETS_ALL_MODES_AB(3314753u),
    OCC_10TICKETS_ALL_MODES_AB(3312577u),
    OCC_24HOURS_ALL_MODES_AB(3322369u),
    OCC_3DAYS_ALL_MODES_AB(3321601u),

    OCC_1TICKET_ALL_MODES_ABC(3310337u),

    OCC_EVENING_UNLIMITED(3305921u),
    OCC_WEEKEND_UNLIMITED(3305985u)
}