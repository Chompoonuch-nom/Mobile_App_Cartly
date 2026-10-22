package com.example.cartly.View;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.example.cartly.R;

/**
 * Toolbar กลางของแอป — สีเขียวธีม Cartly, ชื่อหน้าสีขาว, มีปุ่มย้อนกลับในตัว
 * ใช้ใน layout:  <com.cartly.app.view.ToolbarView android:id="@+id/toolbar" ... />
 * ใช้ใน Activity: binding.toolbar.attachTo(this, "ชื่อหน้า");   // ผูกเป็น ActionBar + ปุ่มย้อนกลับ
 * หน้าแรก (MainActivity) ปรับได้: setBackButtonVisible(false) และ setOnBackClickListener(...)
 */
public class ToolbarView extends Toolbar {
    @Nullable
    private Runnable backAction;


    public ToolbarView(@NonNull Context context) {
        this(context, null);
    }

    public ToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, androidx.appcompat.R.attr.toolbarStyle);
    }

    public ToolbarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(ContextCompat.getColor(context, R.color.light_purple_00));
        setTitleTextColor(ContextCompat.getColor(context, R.color.black));
        setNavigationContentDescription("ย้อนกลับ");
        ViewCompat.setElevation(this, 4 * context.getResources().getDisplayMetrics().density);
        setBackButtonVisible(true);
    }

    /** ผูก Toolbar นี้เป็น ActionBar ของหน้า + ตั้งชื่อหน้า + ให้ปุ่มย้อนกลับทำงานเหมือนกดปุ่ม Back ของระบบ */
    public void attachTo(@NonNull AppCompatActivity activity, @NonNull CharSequence title) {
        activity.setSupportActionBar(this);
        // ต้องตั้งหลัง setSupportActionBar เพราะ ActionBar จะรีเซ็ตชื่อเป็นชื่อแอปตอนผูก
        setTitle(title);
        setBackButtonVisible(true);
        setOnBackClickListener(() -> activity.getOnBackPressedDispatcher().onBackPressed());
    }

    public void setBackButtonVisible(boolean visible) {
        if (visible) setNavigationIcon(R.drawable.ic_arrow_back);
        else setNavigationIcon(null);
    }

    /** กำหนดสิ่งที่เกิดขึ้นเมื่อกดปุ่มย้อนกลับ (ค่าเริ่มต้นจาก attachTo = เหมือนปุ่ม Back ของระบบ) */
    public void setOnBackClickListener(@Nullable Runnable action) {
        backAction = action;
        setNavigationOnClickListener(v -> {
            if (backAction != null) backAction.run();
        });
    }
}
