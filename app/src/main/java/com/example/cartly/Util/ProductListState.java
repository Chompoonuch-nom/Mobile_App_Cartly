package com.example.cartly.Util;

/**
 * บอกหน้า Home ว่า "ข้อมูลสินค้าเปลี่ยนไปแล้ว ควรโหลดรายการใหม่"
 * Home ใช้ interface scroll จึงไม่ควรโหลดใหม่ทุกครั้งที่กลับมาจากหน้าอื่น (ไม่งั้นตำแหน่งที่เลื่อนไว้หาย)
 * ให้โหลดใหม่เฉพาะเมื่อมีใครเรียก markStale() เช่น admin แก้ไข/ลบสินค้า หรือสั่งซื้อสำเร็จ (สต็อกลดลง)
 */
public class ProductListState {
    private static boolean stale = false;

    private ProductListState() {}

    public static void markStale() {
        stale = true;
    }

    /** คืน true หนึ่งครั้งหลัง markStale() แล้วล้างถานะ */
    public static boolean consumeStale() {
        boolean s = stale;
        stale = false;
        return s;
    }
}
