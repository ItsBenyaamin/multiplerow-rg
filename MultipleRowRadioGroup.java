package com.graymind75.vajehyar.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

public class MultipleRowRadioGroup extends LinearLayout implements RadioButton.OnCheckedChangeListener {
    private static final String TAG = "MultipleRowRadioGroup";
    private MultipleRadioGroupListener mListener;
    private ArrayList<RowItem> items = new ArrayList<>();
    private List<RadioButton> radioButtons = new ArrayList<>();
    private RadioButton selectedBtn;
    private String selectedBtnValue;
    private Typeface typeface;

    // to not send selected event when set a default selected item at setSelected()
    private boolean isInitValue = false;

    private static final String textColor = "#0A0A0A";



    public MultipleRowRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setListener(MultipleRadioGroupListener mListener) {
        this.mListener = mListener;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void addItem(String title, String value) {
        RowItem item = new RowItem();
        item.title = title;
        item.value = value;
        item.id = item.hashCode() + items.size();
        items.add(item);
    }

    public void buildItems() {
        if (items.size() < 2) {
            // whats the point? :-/
            return;
        }

        boolean isNeedEmptyAtEnd = false;
        // check if size of items is even for add empty column at end to fix empty space
        if ((items.size() % 2) == 1) {
            isNeedEmptyAtEnd = true;
        }
        ArrayList<RowItem> temp = new ArrayList<>();
        for (int i = 0 ; i < items.size() ; i++) {
            temp.add(items.get(i));
            if (temp.size() == 2) {
                addView(getRow(temp, false));
                temp.clear();
            }
        }
        if (isNeedEmptyAtEnd)
            addView(getRow(temp, true));
    }

    private LinearLayout getRow(ArrayList<RowItem> row, boolean emptyColumn) {
        LinearLayout rowRoot = new LinearLayout(getContext());
        rowRoot.setOrientation(HORIZONTAL);
        rowRoot.setWeightSum(2);
        LayoutParams firstRowLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        firstRowLp.bottomMargin = 8;
        rowRoot.setLayoutParams(firstRowLp);

        for (RowItem column : row) {
            rowRoot.addView(getNewColumn(column.id, column.title));
        }
        if (emptyColumn) {
            rowRoot.addView(getEmptyColumn(), 0);
        }
        return rowRoot;
    }

    private LinearLayout getNewColumn(int id, String title) {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        linearLayout.setLayoutParams(lp);
        linearLayout.setFocusable(true);
        linearLayout.setClickable(true);

        RadioButton rb = getNewRadioButton(id);
        radioButtons.add(rb);
        linearLayout.setOnClickListener(v -> onCheckedChanged(rb, true));

        linearLayout.addView(getNewTextView(title));
        linearLayout.addView(rb);

        return linearLayout;
    }

    private LinearLayout getEmptyColumn() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        linearLayout.setLayoutParams(lp);
        linearLayout.setFocusable(true);
        linearLayout.setClickable(true);
        return linearLayout;
    }

    private RadioButton getNewRadioButton(int id){
        RadioButton radioButton = new RadioButton(getContext());
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        radioButton.setLayoutParams(lp);
        radioButton.setId(id);
        radioButton.setClickable(false);
        radioButton.setFocusable(false);
        return radioButton;
    }

    private AppCompatTextView getNewTextView(String title){
        AppCompatTextView textView = new AppCompatTextView(getContext());
        textView.setText(title);
        LayoutParams lp = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        textView.setLayoutParams(lp);
        textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.parseColor(textColor));
        textView.setPadding(0,0,10, 0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        if (typeface != null) {
            textView.setTypeface(typeface);
        }
        return textView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (selectedBtn != null && buttonView.getId() == selectedBtn.getId()) {
                if (selectedBtnValue != null) mListener.onItemReselect(selectedBtnValue);
                return;
            }
            removeCheck();
            selectedBtn = (RadioButton) buttonView;
            selectedBtn.toggle();
            for (RowItem item : items) {
                if (item.id == buttonView.getId()) {
                    selectedBtnValue = item.value;
                    if (!isInitValue)
                        mListener.onItemSelected(item.value);
                    else
                        isInitValue = false;
                    break;
                }
            }
        }
    }

    private void removeCheck() {
        if (selectedBtn != null) {
            selectedBtn.setChecked(false);
        }
    }

    public void setSelected(String value) {
        isInitValue = true;
        int selectedButtonId = -1;
        for (RowItem item : items) {
            if (item.value.equalsIgnoreCase(value)){
                selectedButtonId = item.id;
                break;
            }
        }
        for (RadioButton rb : radioButtons) {
            if (rb.getId() == selectedButtonId) {
                onCheckedChanged(rb, true);
                break;
            }
        }
    }

    public interface MultipleRadioGroupListener {
        void onItemSelected(String value);
        void onItemReselect(String value);
    }

    private static class RowItem {
        int id;
        String title;
        String value;

        @Override
        public int hashCode() {
            return title.hashCode();
        }
    }
}
