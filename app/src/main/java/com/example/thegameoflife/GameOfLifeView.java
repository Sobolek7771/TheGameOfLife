package com.example.thegameoflife;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class GameOfLifeView extends SurfaceView implements Runnable {

    // Стандартные цвета для живой и мертвой ячейки
    public static final int DEFAULT_ALIVE_COLOR = Color.WHITE;
    public static final int DEFAULT_DEAD_COLOR = Color.BLACK;
    // Поток который отвечает за эволюцию мира
    private Thread thread;
    // Индикатор эволюционирует ли мир
    boolean isRunning = false;
    // Колличество колонок и столбцов
    private int nbRows = 30;
    private int nbColumns = 30;
    // Ширина колонок и столбцов
    public int columnWidth = 1;
    public int rowHeight = 1;
    private World world;
    // Rectangle и Paint используются для отрисовки эллементов
    private Rect r = new Rect();
    private Paint p = new Paint();

    public GameOfLifeView(Context context) {
        super(context);
        initWorld();
    }

    public GameOfLifeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWorld();
    }

    @Override
    public void run() {
        // Пока мир эволюционирует
        while (isRunning) {
            if (!getHolder().getSurface().isValid())
                continue;

            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
            }

            Canvas canvas = getHolder().lockCanvas();
            world.nextGeneration();
            drawCells(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void start() {
        // Мир эволюционирует
        isRunning = true;
        thread = new Thread(this);
        // Начинаю поток для эволюции мира
        thread.start();
    }

    public void stop() {
        isRunning = false;

        while (true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
            break;
        }
    }

    void initWorld() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        // Подсчет columnWidth и rowHeight
        columnWidth = point.x / nbColumns;
        rowHeight = columnWidth;
        world = new World(nbColumns, nbRows);
    }

    // Отрисовка ячеек через Canvas
    private void drawCells(Canvas canvas) {
        for (int i = 0; i < nbColumns; i++) {
            for (int j = 0; j < nbRows; j++) {
                Cell cell = world.get(i, j);
                r.set((cell.x * columnWidth) - 1, (cell.y * rowHeight) - 1,
                        (cell.x * columnWidth + columnWidth) - 1,
                        (cell.y * rowHeight + rowHeight) - 1);
                // Изменение цвета в соответсвии со статусом ячейки
                p.setColor(cell.alive ? DEFAULT_ALIVE_COLOR : DEFAULT_DEAD_COLOR);
                canvas.drawRect(r, p);
            }
        }
    }

    //Изменение состояния клетки по нажатию на экран
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Получем кооридинаты нажатия и конвертируем их для доски
            int i = (int) (event.getX() / columnWidth);
            int j = (int) (event.getY() / rowHeight);
            // Получаем ячейку по позиции нажатия
            Cell cell = world.get(i, j);
            // Инвертируем ячейку по этим координатам
            cell.invert();
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    public boolean isWorldExists(){
        if (world == null){
            return false;
        }else{ return true ;}
    }
}