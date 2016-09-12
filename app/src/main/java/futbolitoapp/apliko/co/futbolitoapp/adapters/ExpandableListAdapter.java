package futbolitoapp.apliko.co.futbolitoapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import futbolitoapp.apliko.co.futbolitoapp.R;
import futbolitoapp.apliko.co.futbolitoapp.SettingsActivity;

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
    private String[] groups = { "Notificación", "¿Cómo jugar?",
            "Cerrar sesión" };
    private String[][] children = { { "Child1" },{ "5 puntos por pronóstico de ganador o empate correcto\n" +"2 puntos por cada marcador correcto\n" + "1 punto por diferencia de gol correcta sin importar ganador" }, { "Salir" } };

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
        textView.setBackgroundResource(R.drawable.fondomarcadores02);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setTextColor(Color.WHITE);
        textView.setCompoundDrawables(null, null, context.getResources().getDrawable(R.drawable.iconopregunta), null);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setText(getChild(groupPosition, childPosition).toString());
        textView.setLayoutParams(lp);
        textView.setBackgroundResource(R.drawable.fondoreglas);
        return textView;
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
                             View convertView, ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
