package cs2340.woms.android.view;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cs2340.woms.view.ListSelectBehavior;

/**
 * An OnItemClickListener which delegates its behavior to a ListSelectBehavior.
 */
public class ItemClickListSelect<T> implements OnItemClickListener {

    private ListSelectBehavior<T> behavior;

    public ItemClickListSelect(ListSelectBehavior<T> behavior) {
        this.behavior = behavior;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        behavior.select((T) parent.getSelectedItem());
    }
}
