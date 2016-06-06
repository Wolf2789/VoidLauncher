package wolfusz.voidlauncher.widget;

import android.view.View;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by wolf2 on 12.04.2016.
 */
public class Widget implements Serializable {
    private static final long serialVersionUID = 9115606718813069507L;

    public enum Type {
        Shortcut,
        Widget
    }

    public WidgetData data;
    public View view;
    public RelativeLayout.LayoutParams params;

    public Widget(Type type, View view, RelativeLayout.LayoutParams params) {
        this.data = new WidgetData(type);
        this.view = view;
        this.params = params;

        this.putData("width", params.width);
        this.putData("height", params.height);
        this.putData("left", params.leftMargin);
        this.putData("right", params.rightMargin);
        this.putData("top", params.topMargin);
        this.putData("bottom", params.bottomMargin);

		/*this.view.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: {

				} break;

				case MotionEvent.ACTION_UP: {

				} break;

				case MotionEvent.ACTION_MOVE: {

				} break;

				}
				return false;
			}
		});*/
        this.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
    }

    // WidgetData.data
    public Object getData(String k) { return this.data.data.get(k); }
    public HashMap<String, Object> getData() { return this.data.data; }

    public void putData(String k, Object v) { this.data.data.put(k, v); }
    public void putData(WidgetData data) { this.data.data.putAll(data.data); }

    // size changing
    public void resizeX(int w) { view.setScaleX(w / view.getWidth()); }
    public void resizeY(int h) { view.setScaleY(h / view.getHeight()); }
    public void resize(int w, int h) { resizeX(w); resizeY(h); }


    // touch events
    public void onLongClick() {

    }
    public void onTouch() {

    }
}
