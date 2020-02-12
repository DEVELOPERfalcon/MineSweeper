package kr.co.taksoft.minesweeper;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    CheckBox flagCheck;
    ImageView reset;
    TextView time;
    ImageView[] btns;

    int[] mine;
    Random rand = new Random();
    int row = 9;
    int column = 9;
    int count=0;
    boolean start = false;
    boolean gameOver;
    boolean gameClear;
    int second = 0;

    Drawable temp1;
    Drawable temp2;
    Bitmap bitmap1;
    Bitmap bitmap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flagCheck = findViewById(R.id.flagcheck);
        reset = findViewById(R.id.reset);
        reset.setOnClickListener(resetListener);
        time = findViewById(R.id.timedisplay);
        btns = new ImageView[81];
        for(int i=0;i<btns.length;i++){
            btns[i] = findViewById(R.id.btn01+i);
            btns[i].setOnClickListener(listener);
        }

        mine = new int[81];
        temp1 = getResources().getDrawable(R.drawable.button);
        temp2 = getResources().getDrawable(R.drawable.flagbutton);
        bitmap1 = ((BitmapDrawable)temp1).getBitmap();
        bitmap2 = ((BitmapDrawable)temp2).getBitmap();
        setting();

    }

    View.OnClickListener resetListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(int i=0;i<btns.length;i++){
                btns[i].setImageResource(R.drawable.button);
                btns[i].setEnabled(true);
            }

            setting();
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(start==false){
                start=true;
                Thread thread = new BackgroundThread();
                thread.start();
            }
            int id = v.getId();
            ImageView btn = findViewById(id);
            Drawable temp = btn.getDrawable();
            Bitmap bitmap = ((BitmapDrawable)temp).getBitmap();
            if(flagCheck.isChecked() && gameClear==false && gameOver==false){
                if(bitmap.equals(bitmap1)) {  //안눌린 버튼이면
                    btn.setImageResource(R.drawable.flagbutton);
                }else{  //깃발있는 버튼이면
                    btn.setImageResource(R.drawable.button);
                }
                return;
            }
            if(bitmap.equals(bitmap2)) return;
            if(Integer.parseInt(btn.getTag().toString())==0){
                //인덱스 번호 구하기
                int index=0;
                for(int i=0;i<btns.length;i++){
                    if(id == btns[i].getId()) index = i;
                }
                checkTagZero(index);
            }else if(Integer.parseInt(btn.getTag().toString())==1){
                btn.setImageResource(R.drawable.one);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==2){
                btn.setImageResource(R.drawable.two);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==3){
                btn.setImageResource(R.drawable.three);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==4){
                btn.setImageResource(R.drawable.four);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==5){
                btn.setImageResource(R.drawable.five);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==6){
                btn.setImageResource(R.drawable.six);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==7){
                btn.setImageResource(R.drawable.seven);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==8){
                btn.setImageResource(R.drawable.eight);
                btn.setEnabled(false);
            }else if(Integer.parseInt(btn.getTag().toString())==9){
                btn.setImageResource(R.drawable.mine);
                for(int i=0;i<btns.length;i++){
                    btns[i].setEnabled(false);
                }
                gameOver = true;
                showGameOver();
            }

            if(checkGameClear()){
                for(int i=0;i<btns.length;i++){
                    btns[i].setEnabled(false);
                }
                gameClear = true;
                showGameClear();
            }
        }
    };

    public void setting(){
        for(int i=0;i<mine.length;i++){
            mine[i]=0;
        }
        for(int i=0;i<10;i++){
            int n = rand.nextInt(81);
            if(mine[n]==1) i--;
            else mine[n] = 1;
        }

        for(int i=0;i<mine.length;i++){
            count=0;
            if(mine[i]==0){ //i번째 위치에 지뢰가 없을 때
                if(i==0){                               //좌상단
                    if(mine[i+1]==1) count++;
                    if(mine[i+column]==1) count++;
                    if(mine[i+column+1]==1) count++;
                }else if(i==(column-1)){                //우상단
                    if(mine[i-1]==1) count++;
                    if(mine[i+column]==1) count++;
                    if(mine[i+column-1]==1) count++;
                }else if(i==(column*(row-1))){          //좌하단
                    if(mine[i+1]==1) count++;
                    if(mine[i-column]==1) count++;
                    if(mine[i-column+1]==1) count++;
                }else if(i==(column*row-1)){            //우하단
                    if(mine[i-1]==1) count++;
                    if(mine[i-column]==1) count++;
                    if(mine[i-column-1]==1) count++;
                }else if(i<column){                     //상단
                    if(mine[i-1]==1) count++;
                    if(mine[i+1]==1) count++;
                    if(mine[i+column-1]==1) count++;
                    if(mine[i+column]==1) count++;
                    if(mine[i+column+1]==1) count++;
                }else if(i%9==0){                     //좌단
                    if(mine[i-column]==1) count++;
                    if(mine[i-column+1]==1) count++;
                    if(mine[i+1]==1) count++;
                    if(mine[i+column]==1) count++;
                    if(mine[i+column+1]==1) count++;
                }else if(i%9==8){                     //우단
                    if(mine[i-column]==1) count++;
                    if(mine[i-column-1]==1) count++;
                    if(mine[i-1]==1) count++;
                    if(mine[i+column-1]==1) count++;
                    if(mine[i+column]==1) count++;
                }else if(i>(column*(row-1)) && i<(column*row-1)){   //하단
                    if(mine[i-1]==1) count++;
                    if(mine[i+1]==1) count++;
                    if(mine[i-column-1]==1) count++;
                    if(mine[i-column]==1) count++;
                    if(mine[i-column+1]==1) count++;
                }else{
                    if(mine[i-column-1]==1) count++;
                    if(mine[i-column]==1) count++;
                    if(mine[i-column+1]==1) count++;
                    if(mine[i-1]==1) count++;
                    if(mine[i+1]==1) count++;
                    if(mine[i+column-1]==1) count++;
                    if(mine[i+column]==1) count++;
                    if(mine[i+column+1]==1) count++;
                }
                btns[i].setTag(count);
            }else{
                btns[i].setTag(9); //지뢰==9
            }
        }
        flagCheck.setChecked(false);
        start = false;
        gameOver = false;
        gameClear = false;
        second = 0;
        time.setText(second+"초");
    }

    public void checkTagZero(int num){
        Drawable temp = btns[num].getDrawable();
        Bitmap bitmap = ((BitmapDrawable)temp).getBitmap();
        if(!btns[num].isEnabled() || bitmap.equals(bitmap2)) return;
        if(Integer.parseInt(btns[num].getTag().toString())==0) {
            btns[num].setImageResource(R.drawable.zero);
            btns[num].setEnabled(false);
        }else{
            if(Integer.parseInt(btns[num].getTag().toString())==1) {
                btns[num].setImageResource(R.drawable.one);
            }else if(Integer.parseInt(btns[num].getTag().toString())==2){
                btns[num].setImageResource(R.drawable.two);
            }else if(Integer.parseInt(btns[num].getTag().toString())==3){
                btns[num].setImageResource(R.drawable.three);
            }else if(Integer.parseInt(btns[num].getTag().toString())==4){
                btns[num].setImageResource(R.drawable.four);
            }else if(Integer.parseInt(btns[num].getTag().toString())==5){
                btns[num].setImageResource(R.drawable.five);
            }else if(Integer.parseInt(btns[num].getTag().toString())==6){
                btns[num].setImageResource(R.drawable.six);
            }else if(Integer.parseInt(btns[num].getTag().toString())==7){
                btns[num].setImageResource(R.drawable.seven);
            }else if(Integer.parseInt(btns[num].getTag().toString())==8){
                btns[num].setImageResource(R.drawable.eight);
            }
            btns[num].setEnabled(false);
            return;
        }
        if((num-column-1)>=0 && num%9!=0) checkTagZero(num-column-1);
        if((num-column)>=0) checkTagZero(num-column);
        if((num-column-1)>=0 && num%9!=8) checkTagZero(num-column+1);
        if(num%9!=0) checkTagZero(num-1);
        if(num%9!=8) checkTagZero(num+1);
        if((num+column-1)<81 && num%9!=0) checkTagZero(num+column-1);
        if((num+column)<81) checkTagZero(num+column);
        if((num+column+1)<81 && num%9!=8) checkTagZero(num+column+1);
    }

    public boolean checkGameClear(){
        if(gameOver==true) return false;
        boolean gameClear = true;
        for(int i=0;i<btns.length;i++){
            if(Integer.parseInt(btns[i].getTag().toString())!=9 && btns[i].isEnabled()==true) gameClear = false;
        }
        return gameClear;
    }

    public void showGameOver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("지뢰를 밟았습니다.");
        builder.setIcon(R.drawable.gameover);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showGameClear(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Clear");
        builder.setMessage("모든 지뢰를 피했습니다.");
        builder.setIcon(R.drawable.gameclear);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class BackgroundThread extends Thread{
        public void run(){
            while (start){
                try{
                    Thread.sleep(1000);
                    if(gameOver==true || gameClear==true || start==false) continue;
                    second++;
                    time.setText(second+"초");
                }catch (InterruptedException ex){
                    Log.e("MineSweeper", "Exception in thread.", ex);
                }
            }
        }
    }
    //
}

