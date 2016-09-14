package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.CambioContrasenia;
import futbolitoapp.apliko.co.futbolitoapp.LoginActivity;
import futbolitoapp.apliko.co.futbolitoapp.R;
import futbolitoapp.apliko.co.futbolitoapp.helper.DataBaseHelper;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_expandable_list_adapte);
//    }


    public ExpandableListAdapter(Context context) {
        this.context = context;
    }

    // Sample data set. children[i] contains the children (String[]) for
    // groups[i].
    private String[] groups = { "Cambiar contraseña", "Notificación", "¿Cómo jugar?",
            "Cerrar sesión" };
    private String[][] children = { { "" },{ "" },{ "5 puntos por pronóstico de ganador o empate correcto\n" +"2 puntos por cada marcador correcto\n" + "1 punto por diferencia de gol correcta sin importar ganador" }, { "Salir" } };

    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        int i = 0;
        try {
            i = children[groupPosition].length;

        } catch (Exception e) {
        }

        return i;
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 100);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        textView.setBackgroundColor(Color.parseColor("#3d3d3d"));
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextColor(Color.WHITE);
        textView.setCompoundDrawables(null, null, context.getResources().getDrawable(R.drawable.iconopregunta), null);
        // Set the text starting position
        textView.setPadding(36, 10, 0, 10);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(context);
        TextView textView = getGenericView();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 200);
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setLayoutParams(lp);
        textView.setBackgroundColor(Color.parseColor("#3d3d3d"));
        linearLayout.addView(textView);
        return linearLayout;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
                             final View convertView, ViewGroup parent) {
        final TextView textView = getGenericView();

        textView.setText(getGroup(groupPosition).toString());
        if(groupPosition == 3){
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    textView.setTextColor(Color.parseColor("#000000"));
                    DataBaseHelper db= new DataBaseHelper(context);
                    db.dropDB();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    return false;
                }
            });
        }
        if(groupPosition == 0){
            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    textView.setTextColor(Color.parseColor("#000000"));
                    DataBaseHelper db= new DataBaseHelper(context);
                    db.dropDB();
                    Intent intent = new Intent(context, CambioContrasenia.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);

                    return false;
                }
            });
        }
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
