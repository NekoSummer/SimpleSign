package cn.xinyue_neko.plugin.simpleSign

interface Config {
    companion object {
        const val SIGN_MONEYS = 1000.00
        const val DATE_TIME_FORMAT = "yyyy-MM-dd"
        const val MESSAGES_SUCCESS = "&b签到成功, 你已获得 {money} 元钱"
        const val MESSAGE_ALREADY = "&7你今天已经签到过了，明天再来吧."
        const val MESSAGE_ERROR = "发生错误: {error}"

        @JvmStatic
        fun formatMoney(amount: Double): String {
            return if (amount == amount.toLong().toDouble()) amount.toLong().toString() else amount.toString()
        }
    }
}