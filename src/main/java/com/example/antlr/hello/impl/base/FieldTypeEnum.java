package com.example.antlr.hello.impl.base;

/**
 * @author chijiuwang
 */
public enum FieldTypeEnum {
    /**
     * 字段类型
     */
    STRING(0, "文本", "string"),
    NUMBER(1, "数值", "number"),
    DATE(2, "日期", "date"),
    DATETIME(3, "时间", "datetime")
    ;

    int code;
    String desc;
    String innerDesc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getInnerDesc() {
        return innerDesc;
    }

    FieldTypeEnum(int code, String desc, String innerDesc) {
        this.code = code;
        this.desc = desc;
        this.innerDesc = innerDesc;
    }

    public static FieldTypeEnum convertByCode(int code) {
        for (FieldTypeEnum t : FieldTypeEnum.values()) {
            if (t.getCode() == code) {
                return t;
            }
        }
        throw new IllegalArgumentException("FieldType unknown.");
    }
    public static String getInnerDescByCode(int code) {
        for (FieldTypeEnum t : FieldTypeEnum.values()) {
            if (t.getCode() == code) {
                return t.innerDesc;
            }
        }
        throw new IllegalArgumentException("FieldType unknown.");
    }

    public static FieldTypeEnum convertByInnerDesc(String str) {
        for (FieldTypeEnum t : FieldTypeEnum.values()) {
            if (t.getInnerDesc().equals(str) ) {
                return t;
            }
        }
        throw new IllegalArgumentException("FieldType unknown.");
    }

}
