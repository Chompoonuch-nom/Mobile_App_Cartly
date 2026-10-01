package com.example.cartly.ResponseDao;

/**
 * ตรงกับ AddressOut (schema.py)
 * ใช้กับ GET/POST/PUT  /api/address */

public class AddressDao {
    private long address_id;
    private String label_type;   // อาจเป็น null เช่น "บ้าน", "ที่ทำงาน"
    private String recipient_name;
    private String phone_number;
    private String address_line;
    private String sub_district;   // ตำบล/แขวง
    private String district;   //อำเภอ/เขต
    private String province;
    private String postal_code;
    private boolean is_default;

    public AddressDao(long address_id, String label_type, String recipient_name, String phone_number, String address_line, String sub_district, String district, String province, String postal_code, boolean is_default) {
        this.address_id = address_id;
        this.label_type = label_type;
        this.recipient_name = recipient_name;
        this.phone_number = phone_number;
        this.address_line = address_line;
        this.sub_district = sub_district;
        this.district = district;
        this.province = province;
        this.postal_code = postal_code;
        this.is_default = is_default;
    }

    public long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(long address_id) {
        this.address_id = address_id;
    }

    public String getLabel_type() {
        return label_type;
    }

    public void setLabel_type(String label_type) {
        this.label_type = label_type;
    }

    public String getRecipient_name() {
        return recipient_name;
    }

    public void setRecipient_name(String recipient_name) {
        this.recipient_name = recipient_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress_line() {
        return address_line;
    }

    public void setAddress_line(String address_line) {
        this.address_line = address_line;
    }

    public String getSub_district() {
        return sub_district;
    }

    public void setSub_district(String sub_district) {
        this.sub_district = sub_district;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public boolean isIs_default() {
        return is_default;
    }

    public void setIs_default(boolean is_default) {
        this.is_default = is_default;
    }
}
