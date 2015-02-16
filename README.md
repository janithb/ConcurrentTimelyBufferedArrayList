# ConcurrentTimelyBufferedArrayList

A new data structure compatible with java.util.List and can be used to stream data in asynchronously.

Example:

ConcurrentTimelyBufferedArrayList<String> list = new ConcurrentTimelyBufferedArrayList<>(10,
                new BufferedListener<String>() {
                    @Override
                    public void accept(List<String> buffered) {
                        System.out.println(buffered.toString());
                    }
                });
        
  list.add("1");
  list.add("2");
  list.add("3");

