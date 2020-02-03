# multiplerow-rg
a class top of LinearLayout to manage RadioButtons with Grid like layout
- just support two columns(Of course you can edit it)

# usage
```
methodTypeRG.addItem("پیشنهاد واژه", Constants.SUGGEST_METHOD);
methodTypeRG.addItem("معنی واژه", Constants.WORD_METHOD);
methodTypeRG.addItem("جست و جوی واژه", Constants.SEARCH_METHOD);
methodTypeRG.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/samim.ttf"));
methodTypeRG.buildItems();
methodTypeRG.setSelected(SharedPreferenceHelper.getInstance().getRequestMethodType());
methodTypeRG.setListener(new MultipleRowRadioGroup.MultipleRadioGroupListener() {
    @Override
    public void onItemSelected(String value) {

    }

    @Override
    public void onItemReselect(String value) {

    }
});
```

# result
![Image of Yaktocat](https://raw.githubusercontent.com/graymind75/multiplerow-rg/master/assets/photo_2020-02-03_14-20-25.jpg)
