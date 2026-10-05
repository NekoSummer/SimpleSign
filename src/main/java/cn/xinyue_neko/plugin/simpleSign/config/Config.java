package cn.xinyue_neko.plugin.simpleSign.config;

public interface Config {
    /** 设置签到数额 **/
    double SIGN_MONEYS = 1000.00;

    /** 年月日 **/
    String DATE_TIME_FORMAT = "yyyy-MM-dd";

    /** 签到消息 **/
    String MESSAGES_SUCCESS = "&b签到成功, 你已获得 {money} 元钱";
    String MESSAGE_ALREADY = "&7你今天已经签到过了，明天再来吧.";
    String MESSAGE_ERROR = "发生错误: {error}";

    /** =============== 这里不要动 =============== **/
    static String formatMoney(double amount) {
        return amount == (long) amount ? String.valueOf((long) amount) : String.valueOf(amount);
    }
}