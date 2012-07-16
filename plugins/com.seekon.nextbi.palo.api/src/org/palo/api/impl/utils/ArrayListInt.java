/*     */ package org.palo.api.impl.utils;
/*     */ 
/*     */ public final class ArrayListInt
/*     */ {
/*     */   int capacity;
/*     */   int[] data;
/*     */   int size;
/*     */   int stamp;
/*     */ 
/*     */   public ArrayListInt(int initialcapacity)
/*     */   {
/* 108 */     if (initialcapacity < 1)
/* 109 */       throw new IllegalArgumentException("initialcapacity of " + 
/* 110 */         initialcapacity + " is illegal.");
/* 111 */     this.data = new int[initialcapacity];
/* 112 */     this.capacity = initialcapacity;
/* 113 */     this.size = 0;
/* 114 */     this.stamp = 0;
/*     */   }
/*     */ 
/*     */   public ArrayListInt()
/*     */   {
/* 124 */     this(8);
/*     */   }
/*     */ 
/*     */   public ArrayListInt(int n, int element)
/*     */   {
/* 135 */     this((n == 0) ? 1 : n);
/* 136 */     for (int i = 0; i < n; ++i)
/* 137 */       add(element);
/*     */   }
/*     */ 
/*     */   public ArrayListInt(int[] array)
/*     */   {
/* 150 */     this(Math.max(1, array.length));
/* 151 */     System.arraycopy(array, 0, this.data, 0, array.length);
/* 152 */     this.size = array.length;
/*     */   }
/*     */ 
/*     */   private final void resize(int newcapacity)
/*     */   {
/* 161 */     if (this.capacity != newcapacity) {
/* 162 */       int[] newdata = new int[newcapacity];
/* 163 */       System.arraycopy(this.data, 0, newdata, 0, this.size);
/* 164 */       this.capacity = newcapacity;
/* 165 */       this.data = newdata;
/* 166 */       this.stamp += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void trimToSize()
/*     */   {
/* 180 */     resize(Math.max(1, this.size));
/*     */   }
/*     */ 
/*     */   public final void ensureCapacity(int newcapacity)
/*     */   {
/* 192 */     if (this.capacity < newcapacity) {
/* 193 */       int desired = this.capacity * 3 / 2 + 1;
/* 194 */       if (desired < newcapacity)
/* 195 */         desired = newcapacity;
/* 196 */       resize(desired);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int size()
/*     */   {
/* 209 */     return this.size;
/*     */   }
/*     */ 
/*     */   public final boolean isEmpty()
/*     */   {
/* 219 */     return this.size == 0;
/*     */   }
/*     */ 
/*     */   public final boolean contains(int element)
/*     */   {
/* 230 */     return indexOf(element) != -1;
/*     */   }
/*     */ 
/*     */   public final int indexOf(int element)
/*     */   {
/* 242 */     int i = 0; for (int len = this.size; i < len; ++i) {
/* 243 */       if (this.data[i] == element)
/* 244 */         return i;
/*     */     }
/* 246 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int lastIndexOf(int element)
/*     */   {
/* 258 */     for (int i = this.size - 1; i >= 0; --i) {
/* 259 */       if (this.data[i] == element)
/* 260 */         return i;
/*     */     }
/* 262 */     return -1;
/*     */   }
/*     */ 
/*     */   public final int get(int index)
/*     */   {
/* 278 */     if ((index >= this.size) || (index < 0))
/* 279 */       throw new IndexOutOfBoundsException("array index " + 
/* 280 */         index + " is out of bounds.");
/* 281 */     return this.data[index];
/*     */   }
/*     */ 
/*     */   public final int set(int index, int element)
/*     */   {
/* 295 */     if ((index < 0) || (index >= this.size))
/* 296 */       throw new IllegalArgumentException("illegal index " + 
/* 297 */         index + " for set method");
/* 298 */     int prev = this.data[index];
/* 299 */     this.data[index] = element;
/*     */ 
/* 301 */     return prev;
/*     */   }
/*     */ 
/*     */   public final boolean add(int element)
/*     */   {
/* 311 */     ensureCapacity(this.size + 1);
/*     */ 
/* 314 */     this.data[this.size] = element;
/* 315 */     this.size += 1;
/* 316 */     this.stamp += 1;
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */   public final boolean addAll(ArrayListInt otherlist)
/*     */   {
/* 329 */     if ((otherlist == null) || (otherlist.size() == 0)) {
/* 330 */       return false;
/*     */     }
/* 332 */     ensureCapacity(this.size + otherlist.size);
/*     */ 
/* 334 */     int[] d = this.data;
/* 335 */     int[] o = otherlist.data;
/* 336 */     int i = this.size; for (int j = 0; j < otherlist.size; ++j) {
/* 337 */       d[i] = o[j];
/*     */ 
/* 336 */       ++i;
/*     */     }
/*     */ 
/* 339 */     this.size += otherlist.size;
/* 340 */     this.stamp += 1;
/* 341 */     return true;
/*     */   }
/*     */ 
/*     */   public final void add(int index, int element)
/*     */   {
/* 357 */     if ((index < 0) || (index > this.size))
/* 358 */       throw new IllegalArgumentException("illegal index " + 
/* 359 */         index + " for add method");
/* 360 */     ensureCapacity(this.size + 1);
/*     */ 
/* 363 */     if (index == this.size) {
/* 364 */       this.data[this.size] = element;
/*     */     }
/*     */     else {
/* 367 */       System.arraycopy(this.data, index, this.data, index + 1, this.size - index);
/* 368 */       this.data[index] = element;
/*     */     }
/* 370 */     this.size += 1;
/* 371 */     this.stamp += 1;
/*     */   }
/*     */ 
/*     */   public final void removeRange(int from, int to)
/*     */   {
/* 385 */     if ((from < 0) || (from >= this.size))
/* 386 */       throw new IllegalArgumentException("arrayindex-from " + from + 
/* 387 */         " out of bounds for removeRange operation");
/* 388 */     if ((to < 0) || (to > this.size))
/* 389 */       throw new IllegalArgumentException("arrayindex-to " + to + 
/* 390 */         " out of bounds for removeRange operation");
/* 391 */     if (from > to) {
/* 392 */       throw new IllegalArgumentException("invalid range [" + from + 
/* 393 */         "," + to + "[ for removeRange operation");
/*     */     }
/* 395 */     if (from == to) return;
/*     */ 
/* 398 */     if (to != this.size) {
/* 399 */       System.arraycopy(this.data, to, this.data, from, this.size - to);
/*     */     }
/* 401 */     this.size -= to - from;
/*     */   }
/*     */ 
/*     */   public final int remove(int index)
/*     */   {
/* 419 */     if ((index < 0) || (index >= this.size))
/* 420 */       throw new IllegalArgumentException("arrayindex " + index + 
/* 421 */         " out of bounds for remove operation");
/*     */     int prev;
/* 423 */     if (index == this.size - 1) {
/* 424 */       prev = this.data[index];
/* 425 */       this.size -= 1;
/*     */     }
/*     */     else {
/* 428 */       prev = this.data[index];
/* 429 */       System.arraycopy(this.data, index + 1, this.data, index, 
/* 430 */         this.data.length - index - 1);
/* 431 */       this.size -= 1;
/*     */     }
/* 433 */     this.stamp += 1;
/*     */ 
/* 435 */     return prev;
/*     */   }
/*     */ 
/*     */   public final void clear()
/*     */   {
/* 443 */     this.size = 0;
/* 444 */     this.stamp += 1;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 456 */     ArrayListInt l = new ArrayListInt(this.capacity);
/* 457 */     System.arraycopy(this.data, 0, l.data, 0, this.size);
/* 458 */     l.size = this.size;
/* 459 */     return l;
/*     */   }
/*     */ 
/*     */   public final int[] toArray()
/*     */   {
/* 470 */     int[] la = new int[this.size];
/* 471 */     System.arraycopy(this.data, 0, la, 0, this.size);
/* 472 */     return la;
/*     */   }
/*     */ 
/*     */   public final int[] toArray(int[] dest)
/*     */   {
/* 494 */     if ((dest == null) || 
/* 495 */       (dest.length < this.size)) {
/* 496 */       return toArray();
/*     */     }
/* 498 */     System.arraycopy(this.data, 0, dest, 0, this.size);
/* 499 */     if (dest.length > this.size) {
/* 500 */       dest[this.size] = 0;
/*     */     }
/* 502 */     return dest;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 512 */     StringBuffer sb = new StringBuffer(256);
/* 513 */     sb.append("ArrayListInt [");
/* 514 */     int i = 0; for (int l = this.size; i < l; ++i) {
/* 515 */       sb.append(this.data[i]);
/* 516 */       if (i == l - 1) continue; sb.append(',');
/*     */     }
/* 518 */     sb.append("]");
/* 519 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   public final void sort()
/*     */   {
/* 531 */     sort(false);
/*     */   }
/*     */ 
/*     */   public final void sort(boolean descending)
/*     */   {
/* 544 */     if (descending)
/* 545 */       qsort_desc(0, this.size - 1);
/*     */     else
/* 547 */       qsort_asc(0, this.size - 1);
/*     */   }
/*     */ 
/*     */   private final void qsort_asc(int low, int high)
/*     */   {
/* 556 */     int l = low;
/* 557 */     int h = high;
/*     */ 
/* 559 */     if (low <= high) {
/* 560 */       int mid = this.data[((low + high) / 2)];
/* 561 */       ///break label110:
								--h;
/* 562 */       ++l;
/*     */       do { if (l < high) if (this.data[l] < mid);
/* 563 */         for (; (h > low) && (this.data[h] > mid); --h);
/* 564 */         if (l <= h) {
/* 565 */           int tmp = this.data[l];
/* 566 */           this.data[l] = this.data[h];
/* 567 */           this.data[h] = tmp;
/* 568 */           ++l;
/* 569 */           ////label110: --h;
/*     */         } }
/*     */ 
/* 561 */       while (l <= h);
/*     */ 
/* 572 */       if (low < h)
/* 573 */         qsort_asc(low, h);
/* 574 */       if (high >= l)
/* 575 */         qsort_asc(l, high);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void qsort_desc(int low, int high)
/*     */   {
/* 584 */     int l = low;
/* 585 */     int h = high;
/*     */ 
/* 587 */     if (low <= high) {
/* 588 */       int mid = this.data[((low + high) / 2)];
/* 589 */       --h;////break label110:
/* 590 */       ++l;
/*     */       do { if (l < high) if (this.data[l] > mid);
/* 591 */         for (; (h > low) && (this.data[h] < mid); --h);
/* 592 */         if (l <= h) {
/* 593 */           int tmp = this.data[l];
/* 594 */           this.data[l] = this.data[h];
/* 595 */           this.data[h] = tmp;
/* 596 */           ++l;
/* 597 */           ////label110: --h;
/*     */         } }
/*     */ 
/* 589 */       while (l <= h);
/*     */ 
/* 600 */       if (low < h)
/* 601 */         qsort_desc(low, h);
/* 602 */       if (high >= l)
/* 603 */         qsort_desc(l, high);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int binarySearch(int element)
/*     */   {
/* 622 */     int l = 0; for (int h = this.size - 1; l <= h; ) {
/* 623 */       int middle = (h + l) / 2;
/* 624 */       int v = this.data[middle];
/* 625 */       if (v == element)
/* 626 */         return middle;
/* 627 */       if (v < element)
/* 628 */         l = middle + 1;
/* 629 */       else if (v > element)
/* 630 */         h = middle - 1;
/*     */     }
/* 632 */     return -1;
/*     */   }
/*     */ 
/*     */   public final boolean replaceAll(int element, int newElement)
/*     */   {
/* 645 */     boolean retval = false;
/* 646 */     int[] d = this.data;
/* 647 */     int i = 0; for (int s = this.size; i < s; ++i) {
/* 648 */       if (d[i] == element) {
/* 649 */         retval = true;
/* 650 */         d[i] = newElement;
/*     */       }
/*     */     }
/* 653 */     return retval;
/*     */   }
/*     */ 
/*     */   public final void reverse()
/*     */   {
/* 661 */     int middle = this.size / 2;
/* 662 */     int[] d = this.data;
/* 663 */     int i = 0; for (int j = this.size - 1; i < middle; --j) {
/* 664 */       int tmp = d[i];
/* 665 */       d[i] = d[j];
/* 666 */       d[j] = tmp;
/*     */ 
/* 663 */       ++i;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final Iterator iterator()
/*     */   {
/* 688 */     return new ListIterator(0);
/*     */   }
/*     */ 
/*     */   public final ListIterator listIterator()
/*     */   {
/* 708 */     return new ListIterator(0);
/*     */   }
/*     */ 
/*     */   public final ListIterator listIterator(int index)
/*     */   {
/* 734 */     return new ListIterator(index);
/*     */   }
/*     */ 
/*     */   public class Iterator
/*     */   {
/*     */     protected int pos;
/*     */     protected final int frozen_stamp;
/*     */ 
/*     */     Iterator()
/*     */     {
/* 764 */       this.pos = 0;
/* 765 */       this.frozen_stamp = ArrayListInt.this.stamp;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 778 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 779 */         throw new ListModifiedException();
/* 780 */       return this.pos != ArrayListInt.this.size;
/*     */     }
/*     */ 
/*     */     public int next()
/*     */     {
/* 791 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 792 */         throw new ListModifiedException();
/* 793 */       int l = ArrayListInt.this.data[(this.pos++)];
/* 794 */       return l;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class ListIterator extends ArrayListInt.Iterator
/*     */   {
/*     */     ListIterator(int index)
/*     */     {
/* 825 */       super();
/*     */ 
/* 827 */       if ((index < 0) || (index > ArrayListInt.this.size()))
/* 828 */         throw new IllegalArgumentException("arrayindex " + index + 
/* 829 */           " is out of bounds for listiterator");
/* 830 */       this.pos = index;
/*     */     }
/*     */ 
/*     */     public int nextIndex()
/*     */     {
/* 841 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 842 */         throw new ListModifiedException();
/* 843 */       if (this.pos == ArrayListInt.this.size) return ArrayListInt.this.size;
/* 844 */       return this.pos + 1;
/*     */     }
/*     */ 
/*     */     public int previousIndex()
/*     */     {
/* 855 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 856 */         throw new ListModifiedException();
/* 857 */       return this.pos - 1;
/*     */     }
/*     */ 
/*     */     public boolean hasPrevious()
/*     */     {
/* 870 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 871 */         throw new ListModifiedException();
/* 872 */       return this.pos > 0;
/*     */     }
/*     */ 
/*     */     public boolean hasNext()
/*     */     {
/* 885 */       if (ArrayListInt.this.stamp != this.frozen_stamp) {
/* 886 */         throw new ListModifiedException();
/*     */       }
/* 888 */       return this.pos < ArrayListInt.this.size;
/*     */     }
/*     */ 
/*     */     public int previous()
/*     */     {
/* 899 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 900 */         throw new ListModifiedException();
/* 901 */       if (hasPrevious()) {
/* 902 */         return ArrayListInt.this.data[(--this.pos)];
/*     */       }
/* 904 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     public int next()
/*     */     {
/* 915 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 916 */         throw new ListModifiedException();
/* 917 */       if (hasNext()) {
/* 918 */         return ArrayListInt.this.data[(this.pos++)];
/*     */       }
/* 920 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     public void set(int element)
/*     */     {
/* 932 */       if (ArrayListInt.this.stamp != this.frozen_stamp)
/* 933 */         throw new ListModifiedException();
/* 934 */       ArrayListInt.this.data[this.pos] = element;
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\workspace\eclipse\opensourceBI\bicp\com.seekon.bicp.palo\lib\paloapi.jar
 * Qualified Name:     org.palo.api.impl.utils.ArrayListInt
 * JD-Core Version:    0.5.4
 */