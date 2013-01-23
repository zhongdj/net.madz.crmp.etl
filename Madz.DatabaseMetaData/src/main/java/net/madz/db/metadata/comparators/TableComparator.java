package net.madz.db.metadata.comparators;

import java.util.Comparator;

import net.madz.db.metadata.Table;
import net.madz.db.utils.MessageConsts;
import net.madz.db.utils.LogUtils;

public class TableComparator implements Comparator<Table> {

    @Override
    // TODO [Jan 22, 2013][barry] [Done]Using readable vocabulary such as one
    // and the other instead t1 and t2
    public int compare(Table one, Table theOther) {
        // TODO [Jan 22, 2013][barry][Done] It seems a String comparator, is
        // there any
        // Open Source resource?
        String firstName = one.getName();
        String secondName = theOther.getName();
        if ( null == firstName || null == secondName ) {
            LogUtils.debug(this.getClass(), MessageConsts.ArgumentShouldNotBeNull);
            throw new IllegalStateException(MessageConsts.ArgumentShouldNotBeNull);
        }
        return firstName.compareTo(secondName);
    }
}
