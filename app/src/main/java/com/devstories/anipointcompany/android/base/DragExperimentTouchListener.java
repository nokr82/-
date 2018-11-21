package com.devstories.anipointcompany.android.base;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by theclub on 5/29/15.
 */
public class DragExperimentTouchListener implements View.OnTouchListener {

    boolean isDragging = false;
    float lastX;
    float lastY;
    float deltaX;
    LockableScrollView lockableScrollView;
    private float screenWidth;
    private Button fromBtn;
    private Button toBtn;
    private int priceType; // 1 : from 2 : to
    private View hilightV;

    public DragExperimentTouchListener(float initalX, float initialY, LockableScrollView lockableScrollView, float screenWidth, Button fromBtn, Button toBtn, int priceType, View hilightV) {
        lastX = initalX;
        lastY = initialY;
        this.lockableScrollView = lockableScrollView;
        this.screenWidth = screenWidth;
        this.fromBtn = fromBtn;
        this.toBtn = toBtn;
        this.priceType = priceType;
        this.hilightV = hilightV;
    }

    @Override
    public boolean onTouch(View arg0, MotionEvent arg1) {

        int action = arg1.getAction();

        if (action == MotionEvent.ACTION_DOWN && !isDragging) {
            isDragging = true;
            deltaX = arg1.getX();
            return true;
        } else if (isDragging) {
            lockableScrollView.setScrollingEnabled(false);

            if (action == MotionEvent.ACTION_MOVE) {
                float x = arg0.getX() + arg1.getX() - deltaX;
                if(soNear(x)) {
                    return true;
                }

                lastX = x;
                arg0.setX(lastX);
                // arg0.setY(arg0.getY());

                int step = findCurrentStep(arg0);
                showPrice(step, arg0);

                return true;
            } else if (action == MotionEvent.ACTION_UP) {
                lockableScrollView.setScrollingEnabled(true);

                isDragging = false;
                // lastX = arg1.getX();
                // lastY = arg1.getY();

                float x = arg0.getX() + arg1.getX() - deltaX;
                if(soNear(x)) {
                    return true;
                }

                lastX = x;
                arg0.setX(lastX);

                int step = findCurrentStep(arg0);
                snap(step, arg0);

                return true;
            } else if (action == MotionEvent.ACTION_CANCEL) {
                lockableScrollView.setScrollingEnabled(true);

                float x = arg0.getX() + arg1.getX() - deltaX;
                arg0.setX(lastX);

                int step = findCurrentStep(arg0);
                snap(step, arg0);

                // arg0.setY(lastY);
                isDragging = false;
                return true;
            }
        }

        return false;
    }

    private boolean soNear(float x) {
        float stepWidth = screenWidth / 6;

        // System.out.println("x : " + x + ", toBtn.getX() : " + toBtn.getX() + ", fromBtn.getX() : " + fromBtn.getX() + ", stepWidth : " + stepWidth + ", priceType : " + priceType);

        if(priceType == 1) {
            if(toBtn.getX() - x - Utils.dpToPx(28) < stepWidth) {
                return true;
            }
        } else {
            if(x - fromBtn.getX() - Utils.dpToPx(28) < stepWidth) {
                return true;
            }
        }
        return false;
    }

    private void snap(int step, View arg0) {
        float stepWidth = screenWidth / 6;

        if(priceType == 1) {
            arg0.setX(stepWidth * step + (stepWidth / 2) - Utils.dpToPx(28));
        } else {
            arg0.setX(stepWidth * step + (stepWidth / 2));
        }

        drawProgress();
    }

    private void showPrice(int step, View arg0) {
        if(priceType == 1) {
            if(step == 0) {
                fromBtn.setText("1");
            } else if(step == 1) {
                fromBtn.setText("3");
            } else if(step == 2) {
                fromBtn.setText("5");
            } else if(step == 3) {
                fromBtn.setText("7");
            } else if(step == 4) {
                fromBtn.setText("10");
            } else if(step == 5) {
                fromBtn.setText("20");
            }
        } else {
            if(step == 0) {
                toBtn.setText("1");
            } else if(step == 1) {
                toBtn.setText("3");
            } else if(step == 2) {
                toBtn.setText("5");
            } else if(step == 3) {
                toBtn.setText("7");
            } else if(step == 4) {
                toBtn.setText("10");
            } else if(step == 5) {
                toBtn.setText("20");
            }
        }


        drawProgress();

    }

    private void drawProgress() {
        float width = Math.abs(toBtn.getX() - fromBtn.getX()) - Utils.dpToPx(28);
        hilightV.setX(fromBtn.getX() + Utils.dpToPx(28));
        hilightV.setLayoutParams(new RelativeLayout.LayoutParams((int) width, (int) Utils.dpToPx(5)));
    }

    private int findCurrentStep(View arg0) {
        float stepWidth = screenWidth / 6;

        float x = arg0.getX();
        if(priceType == 1) {
            x = x + Utils.dpToPx(28) + deltaX;
        }

        // System.out.println("x : " + x + ", stepWidth : " + stepWidth);

        int properStep = 0;
        for(int i = 0; i < 6; i++) {
            if(stepWidth * i <= x && stepWidth * (i + 1) > x) {
                properStep = i;
                break;
            }
        }

        return properStep;
    }
}
