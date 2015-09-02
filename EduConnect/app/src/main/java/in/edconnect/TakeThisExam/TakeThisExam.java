package in.edconnect.TakeThisExam;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.edconnect.Database.QuestionDBHelper;
import in.edconnect.HomePageActivity;
import in.edconnect.R;
import in.edconnect.SelectableTextView.SelectableTextView;
import in.edconnect.TextView.CustomTextView;
import in.edconnect.TouchImageView.TouchImageView;

/**
 * Created by admin on 7/19/2015.
 */
public class TakeThisExam extends Activity implements View.OnTouchListener {

    ArrayList<Question> questionArrayList;
    TextView question;
    TextView reference;
    RadioButton option1,option2,option3,option4;
    Button previous,skip,submit,calculator,protractor,highlight,scale,rotate,answerstats;
    RadioGroup options;
    private int position;
    ArrayList<Answers > answers;
    ImageView protractorImage;
    int _xDelta,_yDelta;
    SharedPreferences highlightText;
    LinearLayout match,rotationcontroller;
    int rotationCon=0;
    int dialerHeight,dialerWidth;
    Bitmap imageOriginal,imageScaled;
    double startAngle;
    Matrix matrix;
    int temp=0;
    ArrayList<HighlightText> highlightTextArrayList=new ArrayList<>();
    MalibuCountDownTimer countDownTimer ;
    private int r=0;
    ImageView mMainImg,mRotateImg;
    EditText match1,match2,match3,match4,match5;
    Button section1,section2,section3,section4,section5,section6;
    QuestionDBHelper dbHelper;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.takethisexam_layout);

        question = (TextView) findViewById(R.id.question);
        option1 = (RadioButton) findViewById(R.id.option1);
        option2 = (RadioButton) findViewById(R.id.option2);
        option3 = (RadioButton) findViewById(R.id.option3);
        option4 = (RadioButton) findViewById(R.id.option4);
        previous = (Button) findViewById(R.id.previous);
        skip = (Button)findViewById(R.id.skip);
        submit = (Button)findViewById(R.id.submit);
        options = (RadioGroup)findViewById(R.id.options);
        reference = (TextView)findViewById(R.id.referencePar);
        calculator = (Button)findViewById(R.id.calculator);
        protractor = (Button)findViewById(R.id.protractor);
        highlight = (Button)findViewById(R.id.highlight);
        scale = (Button)findViewById(R.id.scale);
        protractorImage = (ImageView)findViewById(R.id.protractorimage);
        match = (LinearLayout)findViewById(R.id.match);
        match1=(EditText)findViewById(R.id.answer1);
        match2=(EditText)findViewById(R.id.answer2);
        match3=(EditText)findViewById(R.id.answer3);
        match4=(EditText)findViewById(R.id.answer4);
        match5=(EditText)findViewById(R.id.answer5);





        //////////////////////// Answer stats /////////////////


        answerstats = (Button)findViewById(R.id.answerstatsbutton);
        answerstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////// Send arraylist of answer and and also the section id //////////////
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("list",answers);
                intent.setClass(TakeThisExam.this,AnswerStats.class);
                startActivity(intent);   //// Check if the data of variable is preserved or not
            }
        });

        /////////////////////////////////////////////////////////

        //////////////////////// Section ///////////////////////

        section1 = (Button)findViewById(R.id.section1);
        section2 = (Button)findViewById(R.id.section2);
        section3 = (Button)findViewById(R.id.section3);
        section4 = (Button)findViewById(R.id.section4);
        section5 = (Button)findViewById(R.id.section5);
        section6 = (Button)findViewById(R.id.section6);

        dbHelper = new QuestionDBHelper(this);

        SectionClick sectionClick =  new SectionClick();
        section1.setOnClickListener(sectionClick);
        section2.setOnClickListener(sectionClick);
        section3.setOnClickListener(sectionClick);
        section4.setOnClickListener(sectionClick);
        section5.setOnClickListener(sectionClick);
        section6.setOnClickListener(sectionClick);

        section1.setVisibility(View.INVISIBLE);
        section2.setVisibility(View.INVISIBLE);
        section3.setVisibility(View.INVISIBLE);
        section4.setVisibility(View.INVISIBLE);
        section5.setVisibility(View.INVISIBLE);
        section6.setVisibility(View.INVISIBLE);


        int secCount = 0;
        for(Sections section:dbHelper.getThisSections()){
            switch (secCount){
                case 0:
                    section1.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;

                case 1:
                    section2.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 3:
                    section3.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 4:
                    section4.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 5:
                    section5.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;


                case 6:
                    section6.setText(section.sectionname);
                    /// Store Section Id here //////////////
                    secCount++;
                    break;

            }

        }

        SharedPreferences shareSections = getSharedPreferences("Sections",MODE_PRIVATE);
        SharedPreferences.Editor editor = shareSections.edit();
        editor.putString("section1","false");
        editor.putString("section2","false");
        editor.putString("section3","false");
        editor.putString("section4","false");
        editor.putString("section5","false");
        editor.putString("section6","false");
        editor.apply();


        ///////////////////////////////////////////////////////



        /////////////////////Rotation//////////////////////////////////////

        mMainImg = (ImageView) findViewById(R.id.protractorimage);
        mRotateImg = (ImageView) findViewById(R.id.drager);
        // mRotateImg.setOnTouchListener(this);
        mMainImg.setOnTouchListener(this);
        ///////////




        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.protractor);
        }

        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        protractorImage.setVisibility(View.INVISIBLE);
        highlightText= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        highlightText.edit().clear().commit();

        Typeface faceGautami = Typeface.createFromAsset(getAssets(),
                "gautami.ttf");
        question.setTypeface(faceGautami);
        question.setText("M\'2. ??????????'");

        questionArrayList = new ArrayList<>();
        answers = new ArrayList<>();

        //Add here volley request to fetch all the question and options related to this quiz

        //Add all the question to arraylist
        //in that while loop check if the question is match the following or not if it is then set it
        try{

            saveThisQuestionPaper();

            //if reference bar is not empty and if it is not already added then add it to array list
            questionArrayList.add(new Question(1,0,Html.fromHtml("M\'1. ?????????? ??????? ??????? ???????? ????'").toString(),"Gujarat","Delhi","UP","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(2,0,"The Centre for Cellular and Molecular Biology is situated at?","Patna","Jaipur","Jammu Kashmir","Kerala","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(3,0,"The famous Dilwara Temples are situated in?","Rajasthan","Maharashtra","UP","Himachal","This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(4,0,"What is the capital of India?", "Telangana", "Delhi", "UP", "Maharastra", "This is reference paragraph for you . You have to answer the questions after reading this paragraph",0));
            highlightTextArrayList.add(new HighlightText(0));
            questionArrayList.add(new Question(5,1,"Grand Central Terminal, Park Avenue, New York is the world's?", "largest railway station", "highest railway station", "None", "longest railway station", "",0));
            highlightTextArrayList.add(new HighlightText(1));
            questionArrayList.add(new Question(6,2,"For which of the following disciplines is Nobel Prize awarded??", "Physics and Chemistry", " \tPhysiology or Medicine", "Literature, Peace and Economics", "All of the above", "",1));
            highlightTextArrayList.add(new HighlightText(2));
            questionArrayList.add(new Question(7,3,"Hitler party which came into power in 1933 is known as?", "Labour Party", "Nazi Party", "Democratic Party", "All of the above", "",0));
            highlightTextArrayList.add(new HighlightText(3));
            questionArrayList.get(5).setMatch("one", "two", "three", "four", "five", "2", "1", "5", "4", "3");

        }catch(Exception en){
            Log.e("5",highlightTextArrayList.size()+" "+en.toString());
        }



        //////////  assign time taken from web services

        countDownTimer= new MalibuCountDownTimer(100000,25000);
        countDownTimer.start();


        position=0;
        changeQuestion(position);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(options.getCheckedRadioButtonId()==-1)) {


                    if(option1.getId()==(options.getCheckedRadioButtonId())){
                        answers.add(position, new Answers("1"));

                    }else if(option2.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, new Answers("2"));

                    }else if(option3.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, new Answers("3"));

                    }else if(option4.getId() == (options.getCheckedRadioButtonId())){
                        answers.add(position, new Answers("4"));

                    }
                    Log.e(position+"Answers: ",answers.toString());


                    if(position+1<questionArrayList.size()){
                        position++;
                        changeQuestion(position);
                    }else if(position+1 == questionArrayList.size()){
                          showMeTheDialog();
                    }
                }else{
                    if(questionArrayList.get(position).matchFollowing==1){
                        Log.e("MAnswers: "," 1:" + match1.getText()+" 2:"+match2.getText()+" 3:"+match3.getText()+" 4:"+match4.getText()+" 5:"+match5.getText());
                        answers.add(position, new Answers("1995"));
                        answers.get(position).setMatch(match1.getText().toString(),match2.getText().toString(),match3.getText().toString(),match4.getText().toString(),match5.getText().toString());
                        if(position+1<questionArrayList.size()){
                            position++;
                            changeQuestion(position);
                        }else if(position+1 == questionArrayList.size()){



                            showMeTheDialog();
                        }
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Select One Option!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



         ///////////////////////////////////////////Onclicklistener
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answers.add(position, new Answers("0"));
                if (position + 1 < questionArrayList.size()) {
                    position++;
                    changeQuestion(position);
                }else{
                    int check=0;
                    for(Answers ans:answers){
                        if(ans.ans.equals("0")){

                        }else{
                            check=1;
                        }
                    }
                    if(check==0){
                        Toast.makeText(getApplicationContext(),"You haven't selected any answers!",Toast.LENGTH_SHORT).show();
                        showMeTheDialog();
                    }
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    changeQuestion(position);
                }
            }
        });

        calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName("com.android.calculator2", "com.android.calculator2.Calculator"));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException noSuchActivity) {
                    // handle exception where calculator intent filter is not registered
                }
            }
        });

     /*   rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotationCon==0){
                    rotationCon=1;
                }else{
                    rotationCon=0;
                }
            }
        });*/


        protractor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (protractorImage.getVisibility() == View.INVISIBLE) {

                    protractorImage.setVisibility(View.VISIBLE);
                } else {

                    protractorImage.setVisibility(View.INVISIBLE);
                    try{
                        mRotateImg.setVisibility(View.INVISIBLE);
                    }catch(Exception en){

                    }
                }
            }
        });



       /* protractorImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = protractorImage.getHeight();
                    dialerWidth = protractorImage.getWidth();

                    Log.e("ERROR-1",imageOriginal.getWidth()+" "+imageOriginal.getHeight());
                    Log.e("ERROR0",dialerHeight+ " "+dialerHeight);
                    Log.e("ERROR",((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth())+" "+ (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    // resize
                    Matrix resize = new Matrix();
                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);


                    protractorImage.setImageBitmap(imageScaled);
                    protractorImage.setImageMatrix(matrix);
                }
            }
        });*/



        /*protractorImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                ///Rotation

                if(rotationCon==1){


                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            startAngle = getAngle(event.getX(), event.getY());
                            break;

                        case MotionEvent.ACTION_MOVE:
                            double currentAngle = getAngle(event.getX(), event.getY());
                            rotateDialer((float) (startAngle - currentAngle));
                            startAngle = currentAngle;
                            break;

                        case MotionEvent.ACTION_UP:

                            break;
                    }
                    return true;


                }


                ///////////////




                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                ImageView j = (ImageView) findViewById(R.id.protractorimage);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                        _xDelta = (int) (X - j.getTranslationX());
                        _yDelta = (int) (Y - j.getTranslationY());
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();

                        j.setTranslationX(X - _xDelta);
                        j.setTranslationY(Y - _yDelta);
                        break;
                }

                return true;
            }
        });*/




        highlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int record=position,temp2=0;
                SharedPreferences.Editor editor = highlightText.edit();

                /*if(record!=0 && !questionArrayList.get(record).referencePar.equals(questionArrayList.get(record-1).referencePar)){
                    temp++;
                }
               while(highlightText.getInt(temp+"start"+temp2,-1)!=-1){
                    temp2++;
                }*/
                /*Toast.makeText(getApplicationContext(),"Storing "+record,Toast.LENGTH_SHORT).show();
                editor.putInt(record +  "start", reference.getSelectionStart());
                editor.putInt(record +  "end", reference.getSelectionEnd());
                editor.apply();*/

                //Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+temp+"start",10)+" TO "+highlightText.getInt(record+temp+"end",10),Toast.LENGTH_SHORT).show();
                /*editor.putInt(temp+"start"+temp2,reference.getSelectionStart());
                editor.putInt(temp+"end"+temp2,reference.getSelectionEnd());
                editor.apply();*/


                highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.add(reference.getSelectionStart());
                highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.add(reference.getSelectionEnd());

                Spannable spannable = new SpannableString(reference.getText().toString());
                Log.e("10",highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.toString());
                Log.e("20", highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.toString());
                int it=0;
                for(int start:highlightTextArrayList.get(highlightTextArrayList.get(record).id).start){
                    try {
                        spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.get(it), 0);
                    }catch (Exception e){}
                    it++;
                }

                reference.setText(spannable);
            }
        });






    }








    public void changeQuestion(int position){

        Question questionCurrent = questionArrayList.get(position);


        if(questionCurrent.matchFollowing==1){
            options.setVisibility(View.GONE);
            match.setVisibility(View.VISIBLE);
            matchFollwing(questionCurrent);
            return;
        }

        match.setVisibility(View.GONE);
        options.setVisibility(View.VISIBLE);


        try {


            if(checkForLink(questionCurrent.question)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.question));
                question.setBackground(d);
            }else {
                question.setText(questionCurrent.question);
            }
            if(checkForLink(questionCurrent.option1)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option1));
                option1.setBackground(d);
            }else {
                option1.setText(questionCurrent.option1);
            }
            if(checkForLink(questionCurrent.option2)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option2));
                option2.setBackground(d);
            }else {
                option2.setText(questionCurrent.option2);
            }
            if(checkForLink(questionCurrent.option3)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option3));
                option3.setBackground(d);
            }else {
                option3.setText(questionCurrent.option3);
            }
            if(checkForLink(questionCurrent.option4)){
                Drawable d = new BitmapDrawable(getResources(),decodeThisUrl(questionCurrent.option4));
                option4.setBackground(d);
            }else {
                option4.setText(questionCurrent.option4);
            }


            reference.setText(questionCurrent.referencePar);
        }catch (Exception en){}
        option1.setChecked(false);
        option2.setChecked(false);
        option3.setChecked(false);
        option4.setChecked(false);
        options.clearCheck();

        int record=position,temp2=0;
      /*  if(highlightText.getInt(record+"start",-1)!=-1){
            Toast.makeText(getApplicationContext(),"Getting "+record+temp,Toast.LENGTH_SHORT).show();
            Spannable spannable = new SpannableString(reference.getText().toString());
            try {
                int it=0;
                for(int start:highlightTextArrayList.get(highlightTextArrayList.get(position).id).start){
                    spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(position).id).end.get(it), 0);
                    it++;
                }

            }catch (Exception en){}
            //Toast.makeText(getApplicationContext(),"FROM "+ highlightText.getInt(record+"start",0)+" TO "+highlightText.getInt(record+"end",0),Toast.LENGTH_SHORT).show();
            reference.setText(spannable);
            temp++;
        }*/
        Spannable spannable = new SpannableString(reference.getText().toString());
        Log.e("21",highlightTextArrayList.get(highlightTextArrayList.get(record).id).start.toString());
        Log.e("22",highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.toString());
        int it=0;

        for(int start:highlightTextArrayList.get(highlightTextArrayList.get(record).id).start){
            try {
                spannable.setSpan(new ForegroundColorSpan(Color.BLUE), start, highlightTextArrayList.get(highlightTextArrayList.get(record).id).end.get(it), 0);
            }catch (Exception e){}
                it++;
        }

        reference.setText(spannable);

        try{
            switch (Integer.parseInt(answers.get(position).ans)){
                case 1:
                    option1.setChecked(true);
                    break;
                case 2:
                    option2.setChecked(true);
                    break;
                case 3:
                    option3.setChecked(true);
                    break;
                case 4:
                    option4.setChecked(true);
                    break;
                default:
                    break;
            }
        }catch (Exception en){}

    }



    private boolean checkForLink(String link){

        if(link.contains("http://") || link.contains("https://")){
            return true;
        }
        return false;
    }

    private Bitmap decodeThisUrl(String link){
        URL url=null;
        Bitmap bitmap=null;
        try {
            url = new URL(link);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception en) {
            Log.e("Malformed", "URL EXCEPTION");
        }
        return bitmap;
    }


    @Override
    public void onBackPressed(){
        Toast.makeText(getApplicationContext(),"You can't go back from test!",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();

    }


    public void matchFollwing(Question questionCurrent){

        TextView row12,row13,row14,row15,row21,row22,row23,row24,row25;
        CustomTextView row11;
        EditText answer1,answer2,answer3,answer4,answer5;

        Typeface typeface = Typeface.createFromAsset(getAssets(),"gautami.ttf");

        row11 =(CustomTextView)findViewById(R.id.row11);

        row12 =(TextView)findViewById(R.id.row12);
        row13 =(TextView)findViewById(R.id.row13);
        row14 =(TextView)findViewById(R.id.row14);
        row15 =(TextView)findViewById(R.id.row15);
        row21 =(TextView)findViewById(R.id.row21);
        row22 =(TextView)findViewById(R.id.row22);
        row23 =(TextView)findViewById(R.id.row23);
        row24 =(TextView)findViewById(R.id.row24);
        row25 =(TextView)findViewById(R.id.row25);




        row11.setText("Nahii???????");
        row12.setText(questionCurrent.row12);
        row13.setText(questionCurrent.row13);
        row14.setText(questionCurrent.row14);
        row15.setText(questionCurrent.row15);

        row21.setText(questionCurrent.row21);
        row22.setText(questionCurrent.row22);
        row23.setText(questionCurrent.row23);
        row24.setText(questionCurrent.row24);
        row25.setText(questionCurrent.row25);

    }


    public void showMeTheDialog(){
        final Dialog dialog = new Dialog(TakeThisExam.this);
        LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.comfirmation_popup, null, false);
        dialog.setContentView(v);
        ListView listQuestions = (ListView) v.findViewById(R.id.comfirmation_questions);
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(TakeThisExam.this, questionArrayList, answers);
        listQuestions.setAdapter(adapter);
        dialog.setTitle("Confirm Answers!");

        Button goback = (Button) v.findViewById(R.id.goback);
        Button submit = (Button) v.findViewById(R.id.submit);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                highlightText.edit().clear().commit();

                //// Get the current section id stored in every questions ///////////
                //// And update it's sharedprefereces to true //////////////////////
                //// Check if every shared preferences is true then ////////////////
                //// Start Activity otherwise getSecQuestionAndRefresh /////////////

                startActivity(new Intent(TakeThisExam.this, HomePageActivity.class));
                finish();
            }
        });
        dialog.show();
    }



    /////////////////////////////         TIMER                   ...////////////////////////////////////////////







    public class MalibuCountDownTimer extends CountDownTimer
    {

        public MalibuCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish()
        {
            sendAnswers();
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            updateTicker((millisUntilFinished));
        }
    }












    //////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void sendAnswers(){
        startActivity(new Intent(TakeThisExam.this,HomePageActivity.class));
        finish();
    }

    public void updateTicker(long remaining){
        Toast.makeText(getApplicationContext(),remaining/1000+" ",Toast.LENGTH_SHORT).show();
    }
















    /////////////////////              Rotation            ///////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int eId = event.getAction();
        mRotateImg.setVisibility(ImageView.VISIBLE);
        if (v == mMainImg) {
            switch (eId) {
                case MotionEvent.ACTION_MOVE:
                    drag(v, event);
                    break;
                case MotionEvent.ACTION_UP:
                    mRotateImg.setOnTouchListener(this);
                    break;
                default:
            }
        }
        if (v == mRotateImg) {
            switch (eId) {
                case MotionEvent.ACTION_MOVE:
                    r = r + 2;
                    rotate(v, event);
                    break;
                case MotionEvent.ACTION_UP:
                    mRotateImg.setVisibility(ImageView.INVISIBLE);
                    break;
                default:
            }
            if (v != mMainImg && v != mRotateImg)
                mRotateImg.setVisibility(ImageView.INVISIBLE);
        }
        return true;
    }

    private void rotate(View v, MotionEvent event) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.protractor);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preRotate(-r);
        Bitmap rotaBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix,
                true);
        BitmapDrawable bdr = new BitmapDrawable(rotaBitmap);
        mMainImg.setImageDrawable(bdr);
    }

    private void drag(View v, MotionEvent event) {
        LayoutParams mParams = (LayoutParams) mMainImg.getLayoutParams();
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        mParams.leftMargin=x-150;
        mParams.topMargin =y-210;
        mMainImg.setLayoutParams(mParams);
        mRotateImg.setLayoutParams(mParams);
    }





    /////////////////////////////////////////////////////////////Save this question paper//////////////////////////////



    public void saveThisQuestionPaper(){
        ///// Send volley request and call savethisquestionpaperafterresponse after response
        saveThisQuestionPaperAfterResponse("response in string format");
    }


    public void saveThisQuestionPaperAfterResponse(String response){

        JSONObject questionPaper=null;
        JSONObject QuestionPaper=null;

        response = "{\n" +
                "  \"ExamQuestionPaper\": {\n" +
                "    \"ExamName\": \"Telangana General Knowledge\",\n" +
                "    \"ExamId\": \"000001\",\n" +
                "    \"MaximumMarks\": \"10\",\n" +
                "    \"StartDate\": \"8/18/2015\",\n" +
                "    \"EndDate\": \"8/18/2015\",\n" +
                "    \"StartTime\": \"15-00\",\n" +
                "    \"ExamDuration\": \"00-30\",\n" +
                "    \"Instructions\": \"some sequence of instructions\",\n" +
                "    \"StudentImage\": \"preet.jpg\",\n" +
                "    \"Sections\": {\n" +
                "      \"Section\": [\n" +
                "        {\n" +
                "          \"-id\": \"1\",\n" +
                "          \"-Name\": \"About Telangana\",\n" +
                "          \"SectionQuestions\": {\n" +
                "            \"Question\": [\n" +
                "              {\n" +
                "                \"-id\": \"1\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Which state is formed recently\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Chattishghar\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Jharkhand\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Telangana\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Uttarakhand\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"?????????? ??????? ??????? ???????? ????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"??????? ???\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"???????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"??????????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"2\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana formation day?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"June 1, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"June 2, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"June 3, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"June 4, 2014\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"??????? ???????????? ????????? ????????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"???? 1, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"???? 2, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"???? 3, 2014\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"???? 4, 2014\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"3\",\n" +
                "                \"-CorrectAnswer\": \"1\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana captital\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Hyderabad\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Bangalore\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Vijayawada\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Chennai\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"??????? ??????? ????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"?????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"?????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"???????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"??????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"4\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Telangana first chief minister?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Devendra Fadnavis\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Chandra Sekhar Rao\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Sivaraj Chowhan\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Raman Singh\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"??????? ??????? ????? ??????????? ?????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"???????? ????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"??.????????? ????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"??????? ??????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"???? ?????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"5\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Which is border state of Telangana?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Bihar\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"Maharastra\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Kerala\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Tamilnadu\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"? ??????? ?????? ??? ??????? ???????? ??????????? ?????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"??????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"??????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"????????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"-id\": \"2\",\n" +
                "          \"-Name\": \"About Telugu\",\n" +
                "          \"SectionQuestions\": {\n" +
                "            \"Question\": [\n" +
                "              {\n" +
                "                \"-id\": \"6\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"How is Helen?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"cruel\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"good\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"innocent\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"mad\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"?????? ??????????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"?????? ??????? ????? ????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"??????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"??????????? ??????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"7\",\n" +
                "                \"-CorrectAnswer\": \"1\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Helen helped whom?\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"poor\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"patients\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"disabled\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"unemployed\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"?????? ???????????? ????????? ??? ???????????????? ?????????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"?????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"??????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"??????????????? ??????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"????????? ??????????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"8\",\n" +
                "                \"-CorrectAnswer\": \"2\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-PassageNo\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": \"Helen visiting following countries...\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"Italy, Russia, France\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"England, Africa, America\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"Japan, Koria, China\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"Australia, Nuzeland, Malaysia\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": \"?????? ? ? ?????? ?????????????\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"????, ?????,????????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"?????????, ???????, ???????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"?????, ??????, ????\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"???????????, ???????????, ???????\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"9\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": { \"p\": \"Which triangle is right angle triangle?\" },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"1.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"2.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"3.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"p\": {\n" +
                "                            \"-align\": \"center\",\n" +
                "                            \"-style\": \"text-align:center\",\n" +
                "                            \"span\": {\n" +
                "                              \"-style\": \"font-size:10.0pt\",\n" +
                "                              \"img\": {\n" +
                "                                \"-width\": \"143\",\n" +
                "                                \"-height\": \"136\",\n" +
                "                                \"-src\": \"4.gif\",\n" +
                "                                \"-style\": \"vertical-align: middle\"\n" +
                "                              }\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        { \"-id\": \"1\" },\n" +
                "                        { \"-id\": \"2\" },\n" +
                "                        { \"-id\": \"3\" },\n" +
                "                        { \"-id\": \"4\" }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              },\n" +
                "              {\n" +
                "                \"-id\": \"10\",\n" +
                "                \"-CorrectAnswer\": \"3\",\n" +
                "                \"-QuestionMarks\": \"1\",\n" +
                "                \"-Type\": \"MCQ\",\n" +
                "                \"Laungage\": [\n" +
                "                  {\n" +
                "                    \"-Name\": \"English\",\n" +
                "                    \"QuestionText\": {\n" +
                "                      \"p\": [\n" +
                "                        \"Which pair of the following figures appears to be congruent (same size, same shape)?\",\n" +
                "                        {\n" +
                "                          \"-align\": \"center\",\n" +
                "                          \"-style\": \"text-align:center\",\n" +
                "                          \"span\": {\n" +
                "                            \"-style\": \"font-size:10.0pt\",\n" +
                "                            \"img\": {\n" +
                "                              \"-width\": \"143\",\n" +
                "                              \"-height\": \"136\",\n" +
                "                              \"-src\": \"5.gif\",\n" +
                "                              \"-style\": \"vertical-align: middle\"\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"aaaa\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"bbbb\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"cccc\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"dddd\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  },\n" +
                "                  {\n" +
                "                    \"-Name\": \"Telugu\",\n" +
                "                    \"QuestionText\": {\n" +
                "                      \"p\": [\n" +
                "                        \"Which pair of the following figures appears to be congruent (same size, same shape)?\",\n" +
                "                        {\n" +
                "                          \"-align\": \"center\",\n" +
                "                          \"-style\": \"text-align:center\",\n" +
                "                          \"span\": {\n" +
                "                            \"-style\": \"font-size:10.0pt\",\n" +
                "                            \"img\": {\n" +
                "                              \"-width\": \"143\",\n" +
                "                              \"-height\": \"136\",\n" +
                "                              \"-src\": \"5.gif\",\n" +
                "                              \"-style\": \"vertical-align: middle\"\n" +
                "                            }\n" +
                "                          }\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    },\n" +
                "                    \"Options\": {\n" +
                "                      \"Option\": [\n" +
                "                        {\n" +
                "                          \"-id\": \"1\",\n" +
                "                          \"#text\": \"aaaa\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"2\",\n" +
                "                          \"#text\": \"bbbb\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"3\",\n" +
                "                          \"#text\": \"cccc\"\n" +
                "                        },\n" +
                "                        {\n" +
                "                          \"-id\": \"4\",\n" +
                "                          \"#text\": \"dddd\"\n" +
                "                        }\n" +
                "                      ]\n" +
                "                    }\n" +
                "                  }\n" +
                "                ]\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Images\": {\n" +
                "      \"Image\": [\n" +
                "        {\n" +
                "          \"-id\": \"1\",\n" +
                "          \"-Type\": \"Image\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"-id\": \"2\",\n" +
                "          \"-Type\": \"Image\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"Passages\": {\n" +
                "      \"Passage\": {\n" +
                "        \"-id\": \"1\",\n" +
                "        \"Laungage\": [\n" +
                "          {\n" +
                "            \"-Name\": \"English\",\n" +
                "            \"PassageText\": \"\n" +
                "            Helen is a good girl. She feels pity by seeing poor. She loves nature. She worked for poor people. She visited England, America, Africa and then India.\n" +
                "            \"\n" +
                "          },\n" +
                "          {\n" +
                "            \"-Name\": \"Telugu\",\n" +
                "            \"PassageText\": \"\n" +
                "            ?????? ????? ?????? ??????????.???????, ?????????? ??????????? ??? ???? ???????????.???????? ????? ????? ????? ?????????????.  ?????? ???????? ????? ????? ???? ??? ???????? ????????? ????????\n" +
                "            ???????. ????? ???????? ??? ?????? ??????? ?????, ?? ??????? ????????? ???????????. ??????? ????? ????????? ???????? ?????????? ?????????? ?????? ????? ????? ??????????? ?????? ????? ??. ?????????? ??????,\n" +
                "            '??????????????? ???? ???????? ????? ??????? ???????????????? ????????.? ????????? ?????????, ???????, ?????? ?????? ?????????? ??????????? ???? ????????. ??? ??????, ???????? ??????? ????????????????.\n" +
                "            \"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            questionPaper = new JSONObject(response);
            QuestionPaper = questionPaper.getJSONObject("ExamQuestionPaper");
        }catch (JSONException e){
            Log.e("Parsing",e.toString());
        }


        try{
            String examName = QuestionPaper.getString("ExamName");
            Log.e("ExamName",examName);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String examID = QuestionPaper.getString("ExamId");
            Log.e("ExamId",examID);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String maximumMarks = QuestionPaper.getString("MaximumMarks");
            Log.e("Max Marks",maximumMarks);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            String startDate =  QuestionPaper.getString("StartDate");
            Log.e("Start Date",startDate);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String endDate = QuestionPaper.getString("EndDate");
            Log.e("End Date",endDate);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            String startTime = QuestionPaper.getString("StartTime");
            Log.e("Start Time ",startTime);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String examDuration = QuestionPaper.getString("ExamDuration");
            Log.e("Duration",examDuration);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


        try{
            String instruction = QuestionPaper.getString("Instructions");
            Log.e("Instruction",instruction);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            String studentImageURL = QuestionPaper.getString("StudentImage");
            Log.e("Student URL",studentImageURL);
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }

        try{
            JSONObject sections = QuestionPaper.getJSONObject("Sections");
            JSONArray Section = sections.getJSONArray("Section");
            for(int i=0;i<Section.length();i++){

                JSONObject section = Section.getJSONObject(i);
                String secId="",secName="";
                try {
                     secId = section.getString("-id");
                }catch (JSONException en){
                    Log.e("Error at ",en.toString());
                }
                try{
                     secName = section.getString("-Name");
                }catch (JSONException en){
                    Log.e("Error at ",en.toString());
                }
                Log.e("Name:",secName);

                JSONObject sectionQuestions = section.getJSONObject("SectionQuestions");
                JSONArray Questions = sectionQuestions.getJSONArray("Question");

                //Loop for each question
                for(int j=0;j<Questions.length();j++){

                    ////////////// Create your database from here (id + language = primery key) ///////////////
                    JSONArray questionLanguages=null;
                    try {
                        JSONObject question = Questions.getJSONObject(j);
                        String questionId = question.getString("-id");
                        String questionCorrectAnswer = question.getString("-CorrectAnswer");
                        String questionMarks = question.getString("-QuestionMarks");
                        String questionType = question.getString("-Type");
                        Log.e("InfoQuestion", questionId + " " + questionCorrectAnswer + " " + questionMarks + " " + questionType);

                        questionLanguages = question.getJSONArray("Laungage");
                    }catch (JSONException en){
                        Log.e("Error At",en.toString());
                    }

                    for(int k=0;k<questionLanguages.length();k++){
                        JSONObject questionLanguage=null;
                        try {
                            questionLanguage = questionLanguages.getJSONObject(k);
                            String languageName = questionLanguage.getString("-Name");
                            String languageQuetionText = questionLanguage.getString("QuestionText");
                            Log.e("Language", languageName + " " + languageQuetionText);
                        }catch (JSONException en){
                            Log.e("Error at ",en.toString());
                        }

                        try {

                            JSONObject options = questionLanguage.getJSONObject("Options");
                            JSONArray option = options.getJSONArray("Option");

                            for (int l = 0; l < option.length(); l++) {
                                String optionText = "", optionId = "";
                                JSONObject Option = option.getJSONObject(l);
                                try {
                                    optionId = Option.getString("-id");
                                } catch (JSONException e) {
                                    Log.e("Error At", e.toString());
                                }
                                try {
                                    optionText = Option.getString("#text");
                                } catch (JSONException e) {
                                    Log.e("Error At", e.toString());
                                }
                                Log.e("Option" + l, optionId + " " + optionText);
                            }
                        }catch (JSONException en){
                            Log.e("Error At",en.toString());
                        }

                    }
                }
            }
        }catch(JSONException en){
            Log.e("SaveThisQuestion",en.toString());
        }


    }





    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    //////////////////////////////////////////// Section ////////////////////////////////////////////////////////




    private class SectionClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.section1:
                    getSecQuestionAndRefresh(1);
                    break;

                case R.id.section2:
                    getSecQuestionAndRefresh(2);
                    break;

                case R.id.section3:
                    getSecQuestionAndRefresh(3);
                    break;

                case R.id.section4:
                    getSecQuestionAndRefresh(4);
                    break;

                case R.id.section5:
                    getSecQuestionAndRefresh(5);
                    break;

                case R.id.section6:
                    getSecQuestionAndRefresh(6);
                    break;
            }
        }
    }


    public void getSecQuestionAndRefresh(int sectionid){

        questionArrayList = dbHelper.getThisSectionQuestions(sectionid);
        position=0;
        changeQuestion(position);

    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////



}
