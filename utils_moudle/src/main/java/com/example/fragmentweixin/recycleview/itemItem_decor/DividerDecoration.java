package com.example.fragmentweixin.recycleview.itemItem_decor;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

/**
 * 
 * @author Wei.zhou
 * @date 2018年7月17日
 * @time 下午1:16:09
 * @desc 疑问：getItemOffsets方法参数中outRect参数的大小，会不会受itemview改变
 *            在改变itemview的时候，outRect会不会改变
 *            
 *            目前理解：这个outRect的大小为itemview默认初始化时的大小！outRect不变的大小！
 * 
 * 这个类实现的三个方法执行的顺序执行：itemdecoration-onDraw最先执行，itemview-onDrawOver再执行，itemdecoration-onDrawOver最后执行
 */
public class DividerDecoration extends ItemDecoration {

	private Paint mPaint;
	
	/**
	 * 这个方法作用：设定outRect内部的ItemView所处的位置
	 */
	// 在构造函数里进行绘制的初始化，如画笔属性设置等
    public DividerDecoration() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        // 画笔颜色设置为红色
    }
    
    // 重写getItemOffsets（）方法
    // 作用：设置矩形OutRect 与 Item 的间隔区域
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        System.out.println("前期：getItemOffsets-->left=" + outRect.left +",top="+outRect.top +",right="+outRect.right + ",bottom="+ outRect.bottom);
        
//        int itemPosition = parent.getChildAdapterPosition(view);
        int itemPosition = parent.getChildPosition(view);
        System.out.println("前期：itemPosition=" + itemPosition);
        // 获得每个Item的位置

        // 第1个Item不绘制分割线
//        if (itemPosition != 0) {
            outRect.set(0, 0, 0, 10);
            // 设置间隔区域为10px,即onDraw()可绘制的区域为10px
//        }
        System.out.println("前期：getItemOffsets-->left=" + outRect.left +",top="+outRect.top +",right="+outRect.right + ",bottom="+ outRect.bottom);
        
        System.out.println("前期：itemView[width="+ view.getWidth() + ",height="+view.getHeight()+"]");
    }
    
    // 重写onDraw（）
    // 作用:在间隔区域里绘制一个矩形，即分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        
        // 获取RecyclerView的Child view的个数
        int childCount = parent.getChildCount();

        // 遍历每个Item，分别获取它们的位置信息，然后再绘制对应的分割线
        for ( int i = 0; i < childCount; i++ ) {
            // 获取每个Item的位置
            final View child = parent.getChildAt(i);
            
            System.out.println("onDraw：itemView[width="+ child.getWidth() + ",height="+child.getHeight()+"]");
            
            int index = parent.getChildPosition(child);
            // 第1个Item不需要绘制
//            if ( index == 0 ) {
//                continue;
//            }
            
            // 获取布局参数
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            // 设置矩形(分割线)的宽度为10px
            final int mDivider = 10;

            // 根据子视图的位置 & 间隔区域，设置矩形（分割线）的2个顶点坐标(左上 & 右下)

            // 矩形左上顶点 = (ItemView的左边界,ItemView的下边界)
            // ItemView的左边界 = RecyclerView 的左边界 + paddingLeft距离 后的位置
            final int left = parent.getPaddingLeft();
            // ItemView的下边界：ItemView 的 bottom坐标 + 距离RecyclerView底部距离 +translationY
            final int top = child.getBottom() + params.bottomMargin +
                    Math.round(ViewCompat.getTranslationY(child));

            // 矩形右下顶点 = (ItemView的右边界,矩形的下边界)
            // ItemView的右边界 = RecyclerView 的右边界减去 paddingRight 后的坐标位置
            final int right = parent.getWidth() - parent.getPaddingRight();
            // 绘制分割线的下边界 = ItemView的下边界+分割线的高度
            final int bottom = top + mDivider;


            // 通过Canvas绘制矩形（分割线）
            c.drawRect(left,top,right,bottom,mPaint);
        }
    }
    
    
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
    	super.onDrawOver(c, parent, state);
    }
    
}
