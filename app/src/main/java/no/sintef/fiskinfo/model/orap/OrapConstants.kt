package no.sintef.fiskinfo.model.orap

object OrapConstants {
    const val BOUNDARY_ID_LENGTH = 16
    const val HIDDEN_KL_MESSAGE_RECEIVED_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss+00"
    const val HIDDEN_KL_MESSAGE_OBSERVATION_TIMESTAMP = "yyyyMMddHH00"
    const val HIDDEN_KL_MESSAGE_RECEIVED_FILE_NAME_TIMESTAMP = "yyyyMMdd_HHmmss_000"
    const val SYNOP_DAY1_TIMESTAMP = "dd LLL yyyy"
    const val ACTION_TAG_DATE_FORMAT = "yMMdd"

    object FormDataNames {
        const val ACTION = "Action"
        const val TAG = "tag"
        const val REG_EPOC = "reg_epoc"
        const val USER = "user"
        const val PASSWORD = "password"
        const val MESSAGE_TYPE = "Meldingstype"
        const val HIDDEN_TERMIN = "Hidden_termin"
        const val HIDDEN_MESS = "Hidden_mess"
        const val HIDDEN_KL_MESS = "Hidden_kl_mess"
        const val HIDDEN_KL_STATUS = "Hidden_kl_status"
        const val HIDDEN_FILE = "Hidden_file"
        const val HIDDEN_TYPE = "Hidden_type"
        const val LSTEP = "lstep"
        const val OBS_STATIC = "obs-static"
        const val SYNOP_DAY1 = "synopday1"
        const val SYNOP1 = "synop1"
        const val CAL1 = "cal1"
        const val SIGN_LALA1 = "sign_LaLa1"
        const val LALA1 = "LaLa1"
        const val SIGN_LOLO1 = "sign_LoLo1"
        const val LOLO1 = "LoLo1"
        const val SIGN_TT = "sign_TT"
        const val TT1 = "TT1"
        const val SIGN_TW = "sign_Tw"
        const val TW1 = "Tw1"
        const val TZ1 = "tz1"
        const val FX1 = "fx1"
        const val FG1 = "fg1"
        const val HW1 = "HW1"
        const val PW1 = "PW1"
        const val HW11 = "HW11"
        const val PW11 = "PW11"
        const val DW11 = "DW11"
        const val CI1 = "ci1"
        const val SI1 = "Si1"
        const val BI1 = "bi1"
        const val DI1 = "Di1"
        const val ZI1 = "zi1"
        const val IS1 = "Is1"
        const val ESES1 = "EsEs1"
        const val RS1 = "Rs1"
    }

    object FormValues {
        const val ACTION_CHECK_MESSAGE = "Kontroller melding"
        const val ACTION_SEND_REPORT = "Send melding til met.no"
        const val REPORT_MESSAGE_TYPE = "17-Ship(SPRICE)_1timer_v3"
        const val HIDDEN_FILE_VALUE = "gsmin1??.???"
        const val HIDDEN_TYPE = "17-Ship(SPRICE)_1timer_v3"
        const val LSTEP = 3
    }
}