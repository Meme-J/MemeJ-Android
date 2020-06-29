package com.example.memej.Utils

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout


class FlingBehavior : AppBarLayout.Behavior {
    private var isPositive = false

    constructor()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

//    fun onNestedFling(
//        @NonNull coordinatorLayout: CoordinatorLayout?,
//        @NonNull child: AppBarLayout?, @NonNull target: View, velocityX: Float,
//        velocityY: Float, consumed: Boolean
//    ): Boolean {
//        var velocityY = velocityY
//        var consumed = consumed
//        if (velocityY > 0 && !isPositive || velocityY < 0 && isPositive) {
//            velocityY = velocityY * -1
//        }
//        if (target is RecyclerView && velocityY < 0) {
//            val recyclerView = target as RecyclerView
//            val firstChild: View = recyclerView.getChildAt(0)
//            val childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild)
//            consumed = childAdapterPosition > TOP_CHILD_FLING_THRESHOLD
//        }
//        return super
//            .onNestedFling(coordinatorLayout!!, child!!, target, velocityX, velocityY, consumed)
//    }
//
//    fun onNestedPreScroll(
//        coordinatorLayout: CoordinatorLayout?, child: AppBarLayout?,
//        target: View?, dx: Int, dy: Int, consumed: IntArray?, type: Int
//    ) {
//        super.onNestedPreScroll(coordinatorLayout!!, child!!, target, dx, dy, consumed!!, type)
//        isPositive = dy > 0
//    }

    companion object {
        private const val TOP_CHILD_FLING_THRESHOLD = 3
    }
}