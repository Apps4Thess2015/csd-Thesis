package kiki__000.walkingstoursapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kiki__000 on 13-Jun-15.
 */
public class GraphicsView extends View {

    private static final String QUOTE = "This is a curved text";
    private Path circle;
    private Paint cPaint;
    private Paint tPaint;
    private String text;

    public GraphicsView(Context context, String text) {
        super(context);
        this.text = text;

        int color = Color.argb(127, 255, 0, 255);

        circle = new Path();
        circle.addCircle(200, 400, 100, Path.Direction.CW);

        cPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cPaint.setStyle(Paint.Style.STROKE);
        cPaint.setColor(Color.LTGRAY);
        cPaint.setStrokeWidth(3);

        //setBackgroundResource(R.drawable.circle_button);

        tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        tPaint.setColor(Color.BLACK);
        tPaint.setTextSize(50);}

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawTextOnPath(text, circle, 485, 20, tPaint);
    }

}
