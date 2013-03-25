package net.madz.db.core.meta;

public interface DottedPath {

    /**
     * Parent element in path
     */
    public abstract DottedPath getParent();

    /**
     * Fully qualified name of this element
     * 
     * @return getParent().getAbsoluteName() + "." + getName();
     */
    public abstract String getAbsoluteName();

    /**
     * Local name of this element
     */
    public abstract String getName();

    public abstract DottedPath append(String name);

    public abstract int size();
}