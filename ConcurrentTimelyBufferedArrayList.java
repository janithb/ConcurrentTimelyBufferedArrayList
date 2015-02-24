import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The Class CircularTimelyBufferedArrayList.
 * 
 * @param <E>
 *            the element type
 * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
 * 
 * @since 2.3.0
 */
public class ConcurrentTimelyBufferedArrayList<E> extends AbstractList<E> implements RandomAccess, Serializable,
        Cloneable {

    /** asdas. */
    private static final long serialVersionUID = 8870953621895891238L;

    /** The buff. */
    private final List<E> buff = new CopyOnWriteArrayList<E>();

    /** The listener. */
    private final BufferedListener<E> listener;
    
    /** The consumer. */
    ScheduleConsumer consumer;
    
    /** The time. */
    Timer time;

    /**
     * Instantiates a new circular timely buffered array list.
     * 
     * @param timeInMills
     *            the time in mills
     * @param listener
     *            the listener
     */
    public ConcurrentTimelyBufferedArrayList(final int timeInMills, final BufferedListener<E> listener) {
        this.listener = listener;
        time = new Timer();
        consumer = new ScheduleConsumer();
        time.schedule(consumer, 0, timeInMills);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractList#get(int)
     */
    @Override
    public E get(int index) {
        return buff.get(index);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractList#add(java.lang.Object)
     */
    @Override
    public boolean add(E e) {
        return buff.add(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return buff.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.AbstractCollection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return buff.isEmpty();
    }

    /**
     * Close.
     */
    public void close() {
        time.cancel();
        consumer.cancel();
    }

    /* (non-Javadoc)
     * @see java.util.AbstractList#clear()
     */
    @Override
    public void clear() {
        buff.clear();
    }

    /**
     * Gets the buffered.
     * 
     * @return the buffered
     */
    public List<E> getBuffered() {
        List<E> temp = new ArrayList<>(buff);
        clear();
        return temp;
    }

    /**
     * The Class ScheduleConsumer.
     * 
     * @author <a href="mailto:janith3000@gmail.com">Janith Bandara</a>
     */
    public class ScheduleConsumer extends TimerTask {

        /*
         * (non-Javadoc)
         * 
         * @see java.util.TimerTask#run()
         */
        @Override
        public void run() {
            if (!isEmpty()) {
                listener.accept(getBuffered());
            }
        }

    }

    /**
     * The listener interface for receiving buffered events. The class that is
     * interested in processing a buffered event implements this interface, and
     * the object created with that class is registered with a component using
     * the component's addBufferedListener method. When the buffered event
     * occurs, that object's appropriate method is invoked.
     *
     * @param <E> the element type
     * @see BufferedEvent
     */
    public interface BufferedListener<E> {

        /**
         * Accept.
         * 
         * @param buffered
         *            list
         */
        void accept(List<E> buffered);
    }
}
