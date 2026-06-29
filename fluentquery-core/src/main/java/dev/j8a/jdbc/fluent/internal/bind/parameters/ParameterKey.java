package dev.j8a.jdbc.fluent.internal.bind.parameters;

public final class ParameterKey implements Comparable<ParameterKey>{
    private final int index;
    private final Class<?> type;
    private final ParameterDirection direction;

    public ParameterKey(int index, Class<?> type, ParameterDirection direction) {
        this.index = index;
        this.type = type;
        this.direction = direction;
    }

    public int getIndex() { return index; }
    public Class<?> getType() { return type; }
    public ParameterDirection getDirection() { return direction; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterKey)) return false;
        ParameterKey that = (ParameterKey) o;
        return index == that.index && type.equals(that.type) && direction == that.direction;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + type.hashCode();
        result = 31 * result + direction.hashCode();
        return result;
    }

    @Override
    public int compareTo(ParameterKey o) {
        return Integer.compare(this.index, o.index);
    }
}
