//Please don't replace listeners with lambda!

package com.android.support;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.muffin.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings({"deprecation"})
public class Menu {
    //********** Here you can easily change the menu appearance **********//

    //region Variable
    private static final String TAG = "Mod_Menu"; //Tag for logcat
    private static final String FONT_COLOR = ": <font color='";
    private static final String FONT_COLOR_CLOSURE = "'>";

    private static final int TEXT_COLOR = Color.parseColor("#82CAFD");
    private static final int TEXT_COLOR_2 = Color.parseColor("#FFFFFF");
    private static final int BTN_COLOR = Color.parseColor("#1C262D");
    private static final int MENU_BG_COLOR = Color.parseColor("#EE1C2A35"); //#AARRGGBB
    private static final int MENU_FEATURE_BG_COLOR = Color.parseColor("#DD141C22"); //#AARRGGBB
    private static final int MENU_WIDTH = 290;
    private static final int MENU_HEIGHT = 210;
    private static final int POS_X = 0;
    private static final int POS_Y = 100;

    private static final float MENU_CORNER = 4f;
    private static final int ICON_SIZE = 45; //Change both width and height of image
    private static final float ICON_ALPHA = 0.7f; //Transparent
    public static final String FONT_CLOSURE = "</font>";
    public static final String CBC_4 = "#80CBC4";
    int toggleON = Color.GREEN;
    int toggleOFF = Color.RED;
    int btnON = Color.parseColor("#1b5e20");
    int btnOFF = Color.parseColor("#7f0000");
    int categoryBG = Color.parseColor("#2F3D4C");
    int seekBarColor = Color.parseColor(CBC_4);
    int seekBarProgressColor = Color.parseColor(CBC_4);
    int checkBoxColor = Color.parseColor(CBC_4);
    int radioColor = Color.parseColor("#FFFFFF");
    String numberTxtColor = "#41c300";
    //********************************************************************//

    RelativeLayout mCollapsed, mRootContainer;
    LinearLayout mExpanded, mods, mSettings, mCollapse, mTab1, mTab2, mTab3, mArrowBack;
    LinearLayout.LayoutParams scrollExpanded, scroll;
    WindowManager mWindowManager;
    WindowManager.LayoutParams vmParams;
    ImageView startImage;
    FrameLayout rootFrame;
    ScrollView scrollView;
    boolean stopChecking, overlayRequired;
    Context getContext;

    //initialize methods from the native library
    public static native void Init(Context context, TextView title, TextView subTitle);

    public static native String Icon();

    public static native String IconWebViewData();

    public static native String[] GetFeatureList();

    public static native String[] SettingsList();

    public static native boolean IsGameLibLoaded();

    public static native void setSocialsTitle(TextView textView);

    public static native void setInfoTitle(TextView textView);

    public static native void setCreditTitle(TextView textView);

    public static native String[] subMenuSocials();

    public static native String[] subMenuInfo();

    public static native String[] subMenuCredits();

    //Here we write the code for our Menu
    // Reference: https://www.androidhive.info/2016/11/android-floating-widget-like-facebook-chat-head/
    @SuppressLint("ClickableViewAccessibility")
    public Menu(Context context) {

        getContext = context;
        Preferences.context = context;
        rootFrame = new FrameLayout(context); // Global markup
        rootFrame.setOnTouchListener(onTouchListener());
        mRootContainer = new RelativeLayout(context); // Markup on which two markups of the icon and the menu itself will be placed
        mCollapsed = new RelativeLayout(context); // Markup of the icon (when the menu is minimized)
        mCollapsed.setVisibility(View.VISIBLE);
        mCollapsed.setAlpha(ICON_ALPHA);

        //********** The box of the mod menu **********
        mExpanded = new LinearLayout(context); // Menu markup (when the menu is expanded)
        mExpanded.setVisibility(View.GONE);
        mExpanded.setBackgroundColor(MENU_BG_COLOR);
        mExpanded.setOrientation(LinearLayout.VERTICAL);
        // mExpanded.setPadding(1, 1, 1, 1); //So borders would be visible
        mExpanded.setLayoutParams(new LinearLayout.LayoutParams(dp(MENU_WIDTH), WRAP_CONTENT));
        GradientDrawable gdMenuBody = new GradientDrawable();
        gdMenuBody.setCornerRadius(MENU_CORNER); //Set corner
        gdMenuBody.setColor(MENU_BG_COLOR); //Set background color
        gdMenuBody.setStroke(1, Color.parseColor("#32cb00")); //Set border
        //mExpanded.setBackground(gdMenuBody); //Apply GradientDrawable to it

        //********** The icon to open mod menu **********
        startImage = new ImageView(context);
        startImage.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        int applyDimension = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_SIZE, context.getResources().getDisplayMetrics()); //Icon size
        startImage.getLayoutParams().height = applyDimension;
        startImage.getLayoutParams().width = applyDimension;
        startImage.setScaleType(ImageView.ScaleType.FIT_XY);
        byte[] decode = Base64.decode(Icon(), 0);
        startImage.setImageBitmap(BitmapFactory.decodeByteArray(decode, 0, decode.length));
        ((ViewGroup.MarginLayoutParams) startImage.getLayoutParams()).topMargin = convertDipToPixels();
        //Initialize event handlers for buttons, etc.
        startImage.setOnTouchListener(onTouchListener());
        startImage.setOnClickListener(view -> {
            mCollapsed.setVisibility(View.GONE);
            mExpanded.setVisibility(View.VISIBLE);
        });

        //********** The icon in Webview to open mod menu **********
        WebView wView = new WebView(context); //Icon size width=\"50\" height=\"50\"
        wView.setLayoutParams(new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        int applyDimension2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, ICON_SIZE, context.getResources().getDisplayMetrics()); //Icon size
        wView.getLayoutParams().height = applyDimension2;
        wView.getLayoutParams().width = applyDimension2;
        wView.loadData("<html>" +
                "<head></head>" +
                "<body style=\"margin: 0; padding: 0\">" +
                "<img src=\"" + IconWebViewData() + "\" width=\"" + ICON_SIZE + "\" height=\"" + ICON_SIZE + "\" >" +
                "</body>" +
                "</html>", "text/html", "utf-8");
        wView.setBackgroundColor(0x00000000); //Transparent
        wView.setAlpha(ICON_ALPHA);
        wView.getSettings().setCacheMode(LOAD_CACHE_ELSE_NETWORK);
        wView.setOnTouchListener(onTouchListener());

        //********** Settings icon **********
        TextView settings = new TextView(context); //Android 5 can't show ⚙, instead show other icon instead
        settings.setText("⚙");
        settings.setTextColor(TEXT_COLOR);
        settings.setTypeface(Typeface.DEFAULT_BOLD);
        settings.setTextSize(20.0f);
        RelativeLayout.LayoutParams rlsettings = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rlsettings.addRule(ALIGN_PARENT_RIGHT);
        settings.setLayoutParams(rlsettings);
        settings.setOnClickListener(new View.OnClickListener() {
            boolean settingsOpen;

            @Override
            public void onClick(View v) {
                try {
                    settingsOpen = !settingsOpen;
                    if (settingsOpen) {
                        scrollView.removeAllViews();
                        scrollView.addView(mSettings);
                        scrollView.scrollTo(0, 0);
                    }
                } catch (IllegalStateException ignored) {
                    Log.e(TAG, "onClick: WTF ");
                }
            }
        });

        //********** Settings **********
        mSettings = new LinearLayout(context);
        mSettings.setOrientation(LinearLayout.VERTICAL);
        featureList(SettingsList(), mSettings);

        //********** Title **********
        RelativeLayout titleText = new RelativeLayout(context);
        titleText.setPadding(10, 5, 10, 5);
        titleText.setVerticalGravity(16);

        TextView title = new TextView(context);
        title.setTextColor(TEXT_COLOR);
        title.setTextSize(18.0f);
        title.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_HORIZONTAL);
        title.setLayoutParams(rl);

        //********** Sub title **********
        TextView subTitle = new TextView(context);
        subTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        subTitle.setMarqueeRepeatLimit(-1);
        subTitle.setSingleLine(true);
        subTitle.setSelected(true);
        subTitle.setTextColor(TEXT_COLOR);
        subTitle.setTextSize(10.0f);
        subTitle.setGravity(Gravity.CENTER);
        subTitle.setPadding(0, 0, 0, 5);

        //********** Arrow Back icon **********
        TextView arrowBack = new TextView(context);
        arrowBack.setText("♻");
        arrowBack.setTextColor(TEXT_COLOR);
        arrowBack.setTypeface(Typeface.DEFAULT_BOLD);
        arrowBack.setTextSize(20.0f);
        RelativeLayout.LayoutParams rlsettingsaaa = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rlsettingsaaa.addRule(ALIGN_PARENT_LEFT);
        arrowBack.setLayoutParams(rlsettingsaaa);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;

            @Override
            public void onClick(View v) {
                try {
                    isOpen = !isOpen;
                    if (isOpen) {
                        scrollView.removeAllViews();
                        scrollView.addView(mods);
                        scrollView.scrollTo(0, 0);
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "onClick: Error while clicking back btn");
                }
            }
        });

        mArrowBack = new LinearLayout(context);
        mArrowBack.setOrientation(LinearLayout.VERTICAL);
        featureList(GetFeatureList(), mArrowBack);

        //New Multi Tab Title
        RelativeLayout titleText2 = new RelativeLayout(context);
        titleText2.setPadding(10, 5, 10, 5);
        titleText2.setVerticalGravity(16);

        //********** TAB 1 **********
        TextView subMenuList = new TextView(context);
        subMenuList.setTextColor(TEXT_COLOR);
        subMenuList.setTextSize(18.0f);
        //subMenuList.setGravity(Gravity.LEFT);
        RelativeLayout.LayoutParams rla = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rla.addRule(ALIGN_PARENT_RIGHT);
        subMenuList.setLayoutParams(rla);
        setInfoTitle(subMenuList);
        subMenuList.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;

            @Override
            public void onClick(View v) {
                try {
                    isOpen = !isOpen;
                    if (isOpen) {
                        scrollView.removeAllViews();
                        scrollView.addView(mTab1);
                        scrollView.scrollTo(0, 0);
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "onClick: Error while opening info tab");
                }
            }
        });

        mTab1 = new LinearLayout(context);
        mTab1.setOrientation(LinearLayout.VERTICAL);
        featureList(subMenuInfo(), mTab1);

        //********** TAB 2 **********
        TextView subMenuList2 = new TextView(context);
        subMenuList2.setTextColor(TEXT_COLOR);
        subMenuList2.setTextSize(18.0f);
        //subMenuList2.setGravity(Gravity.CENTER);
        RelativeLayout.LayoutParams rlaa = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rlaa.addRule(RelativeLayout.CENTER_IN_PARENT);
        subMenuList2.setLayoutParams(rlaa);
        setCreditTitle(subMenuList2);
        subMenuList2.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;

            @Override
            public void onClick(View v) {
                try {
                    isOpen = !isOpen;
                    if (isOpen) {
                        scrollView.removeAllViews();
                        scrollView.addView(mTab2);
                        scrollView.scrollTo(0, 0);
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "onClick: Error while opening credit tab");
                }
            }
        });

        mTab2 = new LinearLayout(context);
        mTab2.setOrientation(LinearLayout.VERTICAL);
        featureList(subMenuCredits(), mTab2);

        //********** TAB 3 **********
        TextView subMenuList3 = new TextView(context);
        subMenuList3.setTextColor(TEXT_COLOR);
        subMenuList3.setTextSize(18.0f);
        //subMenuList3.setGravity(Gravity.RIGHT);
        RelativeLayout.LayoutParams rlaaa = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        rlaaa.addRule(ALIGN_PARENT_LEFT);
        subMenuList3.setLayoutParams(rlaaa);
        setSocialsTitle(subMenuList3);
        subMenuList3.setOnClickListener(new View.OnClickListener() {
            boolean isOpen;

            @Override
            public void onClick(View v) {
                try {
                    isOpen = !isOpen;
                    if (isOpen) {
                        scrollView.removeAllViews();
                        scrollView.addView(mTab3);
                        scrollView.scrollTo(0, 0);
                    }
                } catch (IllegalStateException e) {
                    Log.e(TAG, "onClick: Error while opening socials tab");
                }
            }
        });

        mTab3 = new LinearLayout(context);
        mTab3.setOrientation(LinearLayout.VERTICAL);
        featureList(subMenuSocials(), mTab3);

        //********** Mod menu feature list **********
        scrollView = new ScrollView(context);
        //Auto size. To set size manually, change the width and height example 500, 500
        scroll = new LinearLayout.LayoutParams(MATCH_PARENT, dp(MENU_HEIGHT));
        scrollExpanded = new LinearLayout.LayoutParams(mExpanded.getLayoutParams());
        scrollExpanded.weight = 1.0f;
        scrollView.setLayoutParams(Preferences.isExpanded ? scrollExpanded : scroll);
        scrollView.setBackgroundColor(MENU_FEATURE_BG_COLOR);
        mods = new LinearLayout(context);
        mods.setOrientation(LinearLayout.VERTICAL);

        //********** RelativeLayout for buttons **********
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setPadding(10, 3, 10, 3);
        relativeLayout.setVerticalGravity(Gravity.CENTER);

        //**********  Hide/Kill button **********
        RelativeLayout.LayoutParams lParamsHideBtn = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lParamsHideBtn.addRule(ALIGN_PARENT_LEFT);

        Button hideBtn = new Button(context);
        hideBtn.setLayoutParams(lParamsHideBtn);
        hideBtn.setBackgroundColor(Color.TRANSPARENT);
        hideBtn.setText(R.string.hide_kill_hold);
        hideBtn.setTextColor(TEXT_COLOR);
        hideBtn.setOnClickListener(view -> {
            mCollapsed.setVisibility(View.VISIBLE);
            mCollapsed.setAlpha(0);
            mExpanded.setVisibility(View.GONE);
            Toast.makeText(view.getContext(), "Icon hidden. Remember the hidden icon position", Toast.LENGTH_LONG).show();
        });
        hideBtn.setOnLongClickListener(view -> {
            Toast.makeText(view.getContext(), "Menu killed", Toast.LENGTH_LONG).show();
            rootFrame.removeView(mRootContainer);
            mWindowManager.removeView(rootFrame);
            return false;
        });

        //********** Close button **********
        RelativeLayout.LayoutParams lParamsCloseBtn = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lParamsCloseBtn.addRule(ALIGN_PARENT_RIGHT);

        Button closeBtn = new Button(context);
        closeBtn.setLayoutParams(lParamsCloseBtn);
        closeBtn.setBackgroundColor(Color.TRANSPARENT);
        closeBtn.setText(R.string.minimize);
        closeBtn.setTextColor(TEXT_COLOR);
        closeBtn.setOnClickListener(view -> {
            mCollapsed.setVisibility(View.VISIBLE);
            mCollapsed.setAlpha(ICON_ALPHA);
            mExpanded.setVisibility(View.GONE);
        });

        //********** Adding view components **********
        mRootContainer.addView(mCollapsed);
        mRootContainer.addView(mExpanded);
        if (IconWebViewData() != null) {
            mCollapsed.addView(wView);
        } else {
            mCollapsed.addView(startImage);
        }
        titleText.addView(title);
        titleText.addView(settings);

        //**** NEW
        titleText.addView(arrowBack);
        titleText2.addView(subMenuList);
        titleText2.addView(subMenuList2);
        titleText2.addView(subMenuList3);

        //**** END NEW
        mExpanded.addView(titleText);
        mExpanded.addView(subTitle);
        mExpanded.addView(titleText2);
        scrollView.addView(mods);
        mExpanded.addView(scrollView);
        relativeLayout.addView(hideBtn);
        relativeLayout.addView(closeBtn);
        mExpanded.addView(relativeLayout);

        Init(context, title, subTitle);
    }

    public void showMenu() {
        rootFrame.addView(mRootContainer);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            boolean viewLoaded = false;

            @Override
            public void run() {
                //If the save preferences is enabled, it will check if game lib is loaded before starting menu
                //Comment the if-else code out except startService if you want to run the app and test preferences
                if (Preferences.loadPref && !IsGameLibLoaded() && !stopChecking) {
                    if (!viewLoaded) {
                        categoryComponent(mods, "Save preferences was been enabled. Waiting for game lib to be loaded...\n\nForce load menu may not apply mods instantly. You would need to reactivate them again");
                        buttonComponent(mods, -100, "Force load menu");
                        viewLoaded = true;
                    }
                    handler.postDelayed(this, 600);
                } else {
                    mods.removeAllViews();
                    featureList(GetFeatureList(), mods);
                }
            }
        }, 500);
    }

    @SuppressLint("WrongConstant")
    public void setWindowManagerWindowService() {
        //Variable to check later if the phone supports Draw over other apps permission
        int iParams = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? 2038 : 2002;
        vmParams = new WindowManager.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, iParams, 8, -3);
        vmParams.gravity = 51;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = (WindowManager) getContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(rootFrame, vmParams);

        overlayRequired = true;
    }

    @SuppressLint("WrongConstant")
    public void setWindowManagerActivity() {
        vmParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                POS_X, //initial X
                POS_Y, //initial y
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_SPLIT_TOUCH,
                PixelFormat.TRANSPARENT
        );
        vmParams.gravity = 51;
        vmParams.x = POS_X;
        vmParams.y = POS_Y;

        mWindowManager = ((Activity) getContext).getWindowManager();
        mWindowManager.addView(rootFrame, vmParams);
    }

    private View.OnTouchListener onTouchListener() {
        return new View.OnTouchListener() {
            final View collapsedView = mCollapsed;
            final View expandedView = mExpanded;
            private float initialTouchX, initialTouchY;
            private int initialX, initialY;

            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = vmParams.x;
                        initialY = vmParams.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int rawX = (int) (motionEvent.getRawX() - initialTouchX);
                        int rawY = (int) (motionEvent.getRawY() - initialTouchY);
                        mExpanded.setAlpha(1f);
                        mCollapsed.setAlpha(1f);
                        // The check for X diff < 10 && Y diff < 10 because sometime elements moves a little while clicking.
                        // So that is click event.
                        if (rawX < 10 && rawY < 10 && isViewCollapsed()) {
                            // When user clicks on the image view of the collapsed layout,
                            // visibility of the collapsed layout will be changed to "View.GONE"
                            // and expanded view will become visible.
                            try {
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            } catch (NullPointerException e) {
                                Log.e(TAG, "onTouch: WTF ");
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        mExpanded.setAlpha(0.5f);
                        mCollapsed.setAlpha(0.5f);
                        //Calculate the X and Y coordinates of the view.
                        vmParams.x = initialX + ((int) (motionEvent.getRawX() - initialTouchX));
                        vmParams.y = initialY + ((int) (motionEvent.getRawY() - initialTouchY));
                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(rootFrame, vmParams);
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private void featureList(String[] listFT, LinearLayout linearLayout) {
        //Currently looks messy right now. Let me know if you have improvements
        int featNum, subFeat = 0;
        LinearLayout llBak = linearLayout;

        for (int i = 0; i < listFT.length; i++) {
            boolean switchedOn = false;
            String feature = listFT[i];
            if (feature.contains("_True")) {
                switchedOn = true;
                feature = feature.replaceFirst("_True", "");
            }

            linearLayout = llBak;
            if (feature.contains("CollapseAdd_")) {
                linearLayout = mCollapse;
                feature = feature.replaceFirst("CollapseAdd_", "");
            }
            String[] str = feature.split("_");

            //Assign feature number
            if (TextUtils.isDigitsOnly(str[0]) || str[0].matches("-\\d*")) {
                featNum = Integer.parseInt(str[0]);
                feature = feature.replaceFirst(str[0] + "_", "");
                subFeat++;
            } else {
                //Subtract feature number. We don't want to count ButtonLink, Category, RichTextView and RichWebView
                featNum = i - subFeat;
            }
            String[] strSplit = feature.split("_");
            switch (strSplit[0]) {
                case "Toggle":
                    switchComponent(linearLayout, featNum, strSplit[1], switchedOn);
                    break;
                case "SeekBar":
                    seekBarComponent(linearLayout, featNum, strSplit[1], Integer.parseInt(strSplit[2]), Integer.parseInt(strSplit[3]));
                    break;
                case "Button":
                    buttonComponent(linearLayout, featNum, strSplit[1]);
                    break;
                case "ButtonOnOff":
                    buttonOnOffComponent(linearLayout, featNum, strSplit[1], switchedOn);
                    break;
                case "Spinner":
                    textViewComponent(linearLayout, strSplit[1]);
                    spinnerComponent(linearLayout, featNum, strSplit[1], strSplit[2]);
                    break;
                case "InputText":
                    inputTextComponent(linearLayout, featNum, strSplit[1]);
                    break;
                case "InputValue":
                    if (strSplit.length == 3)
                        inputNumComponent(linearLayout, featNum, strSplit[2], Integer.parseInt(strSplit[1]));
                    if (strSplit.length == 2)
                        inputNumComponent(linearLayout, featNum, strSplit[1], 0);
                    break;
                case "CheckBox":
                    checkBoxComponent(linearLayout, featNum, strSplit[1], switchedOn);
                    break;
                case "RadioButton":
                    radioButtonComponent(linearLayout, featNum, strSplit[1], strSplit[2]);
                    break;
                case "Collapse":
                    collapseComponent(linearLayout, strSplit[1], switchedOn);
                    subFeat++;
                    break;
                case "ButtonLink":
                    subFeat++;
                    buttonLinkComponent(linearLayout, strSplit[1], strSplit[2]);
                    break;
                case "Category":
                    subFeat++;
                    categoryComponent(linearLayout, strSplit[1]);
                    break;
                case "RichTextView":
                    subFeat++;
                    textViewComponent(linearLayout, strSplit[1]);
                    break;
                case "RichWebView":
                    subFeat++;
                    webTextViewComponent(linearLayout, strSplit[1]);
                    break;
                default:
                    break;
            }
        }
    }

    private void switchComponent(LinearLayout linLayout, final int featNum, final String featName, boolean swiOn) {
        final Switch switchR = new Switch(getContext);
        ColorStateList buttonStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_checked},
                        new int[]{}
                },
                new int[]{
                        Color.BLUE,
                        toggleON, // ON
                        toggleOFF // OFF
                }
        );
        //Set colors of the switch. Comment out if you don't like it
        try {
            switchR.getThumbDrawable().setTintList(buttonStates);
            switchR.getTrackDrawable().setTintList(buttonStates);
        } catch (NullPointerException ex) {
            Log.d(TAG, String.valueOf(ex));
        }
        switchR.setText(featName);
        switchR.setTextColor(TEXT_COLOR_2);
        switchR.setPadding(10, 5, 0, 5);
        switchR.setChecked(Preferences.loadPrefBool(featName, featNum, swiOn));
        switchR.setOnCheckedChangeListener((compoundButton, bool) -> {
            Preferences.changeFeatureBool(featName, featNum, bool);
            switch (featNum) {
                case -1: //Save perferences
                    Preferences.with(switchR.getContext()).writeBoolean(-1, bool);
                    if (!bool)
                        Preferences.with(switchR.getContext()).clear(); // Clear preferences if switched off
                    break;
                case -3:
                    Preferences.isExpanded = bool;
                    scrollView.setLayoutParams(bool ? scrollExpanded : scroll);
                    break;
                default:
                    break;
            }
        });

        linLayout.addView(switchR);
    }

    private void seekBarComponent(LinearLayout linLayout, final int featNum, final String featName, final int min, int max) {
        int loadedProg = Preferences.loadPrefInt(featName, featNum);
        LinearLayout linearLayout = new LinearLayout(getContext);
        linearLayout.setPadding(10, 5, 0, 5);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        final TextView textView = new TextView(getContext);
        textView.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + ((loadedProg == 0) ? min : loadedProg)));
        textView.setTextColor(TEXT_COLOR_2);

        SeekBar seekBar = new SeekBar(getContext);
        seekBar.setPadding(25, 10, 35, 10);
        seekBar.setMax(max);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            seekBar.setMin(min); //setMin for Oreo and above
        seekBar.setProgress((loadedProg == 0) ? min : loadedProg);
        seekBar.getThumb().setColorFilter(seekBarColor, PorterDuff.Mode.SRC_ATOP);
        seekBar.getProgressDrawable().setColorFilter(seekBarProgressColor, PorterDuff.Mode.SRC_ATOP);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* Nothing here */
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /* Nothing here */
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                //if progress is greater than minimum, don't go below. Else, set progress
                seekBar.setProgress(Math.max(i, min));
                Preferences.changeFeatureInt(featName, featNum, Math.max(i, min));
                textView.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + (Math.max(i, min))));
            }
        });
        linearLayout.addView(textView);
        linearLayout.addView(seekBar);

        linLayout.addView(linearLayout);
    }

    private void buttonComponent(LinearLayout linLayout, final int featNum, final String featName) {
        final Button button = new Button(getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setTextColor(TEXT_COLOR_2);
        button.setAllCaps(false); //Disable caps to support html
        button.setText(Html.fromHtml(featName));
        button.setBackgroundColor(BTN_COLOR);
        button.setOnClickListener(v -> {
            switch (featNum) {

                case -6:
                    scrollView.removeView(mSettings);
                    scrollView.addView(mods);
                    break;
                case -100:
                    stopChecking = true;
                    break;
                default:
                    break;
            }
            Preferences.changeFeatureInt(featName, featNum, 0);
        });

        linLayout.addView(button);
    }

    private void buttonLinkComponent(LinearLayout linLayout, final String featName, final String url) {
        final Button button = new Button(getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setAllCaps(false); //Disable caps to support html
        button.setTextColor(TEXT_COLOR_2);
        button.setText(Html.fromHtml(featName));
        button.setBackgroundColor(BTN_COLOR);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse(url));
            getContext.startActivity(intent);
        });
        linLayout.addView(button);
    }

    private void buttonOnOffComponent(LinearLayout linLayout, final int featNum, String featName, boolean switchedOn) {
        final Button button = new Button(getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.setMargins(7, 5, 7, 5);
        button.setLayoutParams(layoutParams);
        button.setTextColor(TEXT_COLOR_2);
        button.setAllCaps(false); //Disable caps to support html

        final String finalfeatName = featName.replace("OnOff_", "");
        boolean isOn = Preferences.loadPrefBool(featName, featNum, switchedOn);
        if (isOn) {
            button.setText(Html.fromHtml(finalfeatName + ": ON"));
            button.setBackgroundColor(btnON);
            isOn = false;
        } else {
            button.setText(Html.fromHtml(finalfeatName + ": OFF"));
            button.setBackgroundColor(btnOFF);
            isOn = true;
        }
        final boolean finalIsOn = isOn;
        button.setOnClickListener(new View.OnClickListener() {
            boolean isOn = finalIsOn;

            public void onClick(View v) {
                Preferences.changeFeatureBool(finalfeatName, featNum, isOn);
                if (isOn) {
                    button.setText(Html.fromHtml(finalfeatName + ": ON"));
                    button.setBackgroundColor(btnON);
                    isOn = false;
                } else {
                    button.setText(Html.fromHtml(finalfeatName + ": OFF"));
                    button.setBackgroundColor(btnOFF);
                    isOn = true;
                }
            }
        });
        linLayout.addView(button);
    }

    /**
     * @noinspection rawtypes
     */
    private void spinnerComponent(LinearLayout linLayout, final int featNum, final String featName, final String list) {
        Log.d(TAG, "spinner " + featNum + " " + featName + " " + list);
        final List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));

        // Create another LinearLayout as a workaround to use it as a background
        // to keep the down arrow symbol. No arrow symbol if setBackgroundColor set
        LinearLayout linearLayout2 = new LinearLayout(getContext);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams2.setMargins(7, 2, 7, 2);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout2.setBackgroundColor(BTN_COLOR);
        linearLayout2.setLayoutParams(layoutParams2);

        final Spinner spinner = new Spinner(getContext, Spinner.MODE_DROPDOWN);
        spinner.setLayoutParams(layoutParams2);
        spinner.getBackground().setColorFilter(1, PorterDuff.Mode.SRC_ATOP); //trick to show white down arrow color
        //Creating the ArrayAdapter instance having the list
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext, android.R.layout.simple_spinner_dropdown_item, lists);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner'
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(Preferences.loadPrefInt(featName, featNum));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Preferences.changeFeatureInt(spinner.getSelectedItem().toString(), featNum, position);
                ((TextView) parentView.getChildAt(0)).setTextColor(TEXT_COLOR_2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                /* Nothing here */
            }
        });
        linearLayout2.addView(spinner);
        linLayout.addView(linearLayout2);
    }

    private void inputNumComponent(LinearLayout linLayout, final int featNum, final String featName, final int maxValue) {
        LinearLayout linearLayout = new LinearLayout(getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.setMargins(7, 5, 7, 5);

        final Button button = new Button(getContext);
        int num = Preferences.loadPrefInt(featName, featNum);
        button.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + ((num == 0) ? 1 : num) + FONT_CLOSURE));
        button.setAllCaps(false);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(BTN_COLOR);
        button.setTextColor(TEXT_COLOR_2);
        button.setOnClickListener(view -> {
            AlertDialog.Builder alertName = new AlertDialog.Builder(getContext);
            final EditText editText = new EditText(getContext);
            if (maxValue != 0)
                editText.setHint("Max value: " + maxValue);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setKeyListener(DigitsKeyListener.getInstance("0123456789-"));
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(10);
            editText.setFilters(filterArray);
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            });
            editText.requestFocus();

            alertName.setTitle("Input number");
            alertName.setView(editText);
            LinearLayout layoutName = new LinearLayout(getContext);
            layoutName.setOrientation(LinearLayout.VERTICAL);
            layoutName.addView(editText); // displays the user input bar
            alertName.setView(layoutName);

            alertName.setPositiveButton("OK", (dialog, whichButton) -> {
                int num1;
                try {
                    num1 = Integer.parseInt(TextUtils.isEmpty(editText.getText().toString()) ? "0" : editText.getText().toString());
                    if (maxValue != 0 && num1 >= maxValue)
                        num1 = maxValue;
                } catch (NumberFormatException ex) {
                    if (maxValue != 0)
                        num1 = maxValue;
                    else
                        num1 = 2147483640;
                }

                button.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + num1 + FONT_CLOSURE));
                Preferences.changeFeatureInt(featName, featNum, num1);

                editText.setFocusable(false);
            });

            alertName.setNegativeButton("Cancel", (dialog, whichButton) -> {
                // dialog.cancel(); // closes dialog
                InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            });

            if (overlayRequired) {
                AlertDialog dialog = alertName.create(); // display the dialog
                Objects.requireNonNull(dialog.getWindow()).setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
                dialog.show();
            } else {
                alertName.show();
            }
        });

        linearLayout.addView(button);
        linLayout.addView(linearLayout);
    }

    private void inputTextComponent(LinearLayout linLayout, final int featNum, final String featName) {
        LinearLayout linearLayout = new LinearLayout(getContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParams.setMargins(7, 5, 7, 5);

        final Button button = new Button(getContext);

        String string = Preferences.loadPrefString(featName, featNum);
        button.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + string + FONT_CLOSURE));

        button.setAllCaps(false);
        button.setLayoutParams(layoutParams);
        button.setBackgroundColor(BTN_COLOR);
        button.setTextColor(TEXT_COLOR_2);
        button.setOnClickListener(view -> {
            AlertDialog.Builder alertName = new AlertDialog.Builder(getContext);

            final EditText editText = new EditText(getContext);
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                } else {
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
            });
            editText.requestFocus();

            alertName.setTitle("Input text");
            alertName.setView(editText);
            LinearLayout layoutName = new LinearLayout(getContext);
            layoutName.setOrientation(LinearLayout.VERTICAL);
            layoutName.addView(editText); // displays the user input bar
            alertName.setView(layoutName);

            alertName.setPositiveButton("OK", (dialog, whichButton) -> {
                String str = editText.getText().toString();
                button.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + str + FONT_CLOSURE));
                Preferences.changeFeatureString(featName, featNum, str);
                editText.setFocusable(false);
            });

            alertName.setNegativeButton("Cancel", (dialog, whichButton) -> {
                //dialog.cancel(); // closes dialog
                InputMethodManager imm = (InputMethodManager) getContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            });


            if (overlayRequired) {
                AlertDialog dialog = alertName.create(); // display the dialog
                Objects.requireNonNull(dialog.getWindow()).setType(Build.VERSION.SDK_INT >= 26 ? 2038 : 2002);
                dialog.show();
            } else {
                alertName.show();
            }
        });

        linearLayout.addView(button);
        linLayout.addView(linearLayout);
    }

    private void checkBoxComponent(LinearLayout linLayout, final int featNum, final String featName, boolean switchedOn) {
        final CheckBox checkBox = new CheckBox(getContext);
        checkBox.setText(featName);
        checkBox.setTextColor(TEXT_COLOR_2);
        checkBox.setButtonTintList(ColorStateList.valueOf(checkBoxColor));
        checkBox.setChecked(Preferences.loadPrefBool(featName, featNum, switchedOn));
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> Preferences.changeFeatureBool(featName, featNum, isChecked));
        linLayout.addView(checkBox);
    }

    private void radioButtonComponent(LinearLayout linLayout, final int featNum, String featName, final String list) {
        //Credit: LoraZalora
        final List<String> lists = new LinkedList<>(Arrays.asList(list.split(",")));

        final TextView textView = new TextView(getContext);
        textView.setText(String.format("%s:", featName));
        textView.setTextColor(TEXT_COLOR_2);

        final RadioGroup radioGroup = new RadioGroup(getContext);
        radioGroup.setPadding(10, 5, 10, 5);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.addView(textView);

        for (int i = 0; i < lists.size(); i++) {
            final RadioButton radioButton = new RadioButton(getContext);
            final String finalFeatName = featName, radioName = lists.get(i);
            View.OnClickListener firstRadioListener = v -> {
                textView.setText(Html.fromHtml(finalFeatName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + radioName));
                Preferences.changeFeatureInt(finalFeatName, featNum, radioGroup.indexOfChild(radioButton));
            };
            radioButton.setText(lists.get(i));
            radioButton.setTextColor(Color.LTGRAY);
            radioButton.setButtonTintList(ColorStateList.valueOf(radioColor));
            radioButton.setOnClickListener(firstRadioListener);
            radioGroup.addView(radioButton);
        }

        int index = Preferences.loadPrefInt(featName, featNum);
        if (index > 0) { //Preventing it to get an index less than 1. below 1 = null = crash
            textView.setText(Html.fromHtml(featName + FONT_COLOR + numberTxtColor + FONT_COLOR_CLOSURE + lists.get(index - 1)));
            ((RadioButton) radioGroup.getChildAt(index)).setChecked(true);
        }
        linLayout.addView(radioGroup);
    }

    private void collapseComponent(LinearLayout linLayout, final String text, final boolean expanded) {
        LinearLayout.LayoutParams layoutParamsLL = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        layoutParamsLL.setMargins(0, 5, 0, 0);

        LinearLayout collapse = new LinearLayout(getContext);
        collapse.setLayoutParams(layoutParamsLL);
        collapse.setVerticalGravity(16);
        collapse.setOrientation(LinearLayout.VERTICAL);

        final LinearLayout collapseSub = new LinearLayout(getContext);
        collapseSub.setVerticalGravity(16);
        collapseSub.setPadding(0, 5, 0, 5);
        collapseSub.setOrientation(LinearLayout.VERTICAL);
        collapseSub.setBackgroundColor(Color.parseColor("#222D38"));
        collapseSub.setVisibility(View.GONE);
        mCollapse = collapseSub;

        final TextView textView = new TextView(getContext);
        textView.setBackgroundColor(categoryBG);
        textView.setText(String.format("▽ %s ▽", text));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(TEXT_COLOR_2);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 20, 0, 20);

        if (expanded) {
            collapseSub.setVisibility(View.VISIBLE);
            textView.setText(String.format("△ %s △", text));
        }

        textView.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = expanded;

            @Override
            public void onClick(View v) {

                boolean z = !isChecked;
                isChecked = z;
                if (z) {
                    collapseSub.setVisibility(View.VISIBLE);
                    textView.setText(String.format("△ %s △", text));
                    return;
                }
                collapseSub.setVisibility(View.GONE);
                textView.setText(String.format("▽ %s ▽", text));
            }
        });
        collapse.addView(textView);
        collapse.addView(collapseSub);
        linLayout.addView(collapse);
    }

    private void categoryComponent(LinearLayout linLayout, String text) {
        TextView textView = new TextView(getContext);
        textView.setBackgroundColor(categoryBG);
        textView.setText(Html.fromHtml(text));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(TEXT_COLOR_2);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(0, 5, 0, 5);
        linLayout.addView(textView);
    }

    private void textViewComponent(LinearLayout linLayout, String text) {
        TextView textView = new TextView(getContext);
        textView.setText(Html.fromHtml(text));
        textView.setTextColor(TEXT_COLOR_2);
        textView.setPadding(10, 5, 10, 5);
        linLayout.addView(textView);
    }

    private void webTextViewComponent(LinearLayout linLayout, String text) {
        WebView wView = new WebView(getContext);
        wView.loadData(text, "text/html", "utf-8");
        wView.setBackgroundColor(0x00000000); //Transparent
        wView.setPadding(0, 5, 0, 5);
        wView.getSettings().setCacheMode(LOAD_CACHE_ELSE_NETWORK);
        linLayout.addView(wView);
    }

    private boolean isViewCollapsed() {
        return rootFrame == null || mCollapsed.getVisibility() == View.VISIBLE;
    }

    //For our image a little converter
    private int convertDipToPixels() {
        return (int) (((10) * getContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    private int dp(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, getContext.getResources().getDisplayMetrics());
    }

    public void setVisibility(int view) {
        if (rootFrame != null) {
            rootFrame.setVisibility(view);
        }
    }

    public void onDestroy() {
        if (rootFrame != null) {
            mWindowManager.removeView(rootFrame);
        }
    }
}
