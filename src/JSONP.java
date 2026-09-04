import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A standalone exercise for parsing and validating JSON strings from standard input.
 * It manually counts objects and keys without external JSON libraries.
 */
public class JSONP {

  public static void main(String[] args) {
    // Read JSON string from standard input
    Scanner input = new Scanner(System.in);
    String JSON = input.nextLine();
    if (firstCheck(JSON)) {
      JSON = JSON.substring(1, JSON.length());
    }
    if (keyCheck(JSON)) {
      JSON = "{" + JSON;
      JSON = JSON.replaceAll(" ", "");
      int answer = 0;

      // Count total elements in valid JSON object
      for (int i = 1; i <= objectCounter(JSON); i++) {
        String st = subObj(JSON, i);
        answer += JSONcounter(st);
      }
      answer++;
      System.out.println(answer); // Print element count
    } else {
      System.out.println(0); // Invalid JSON
    }
    input.close();
  }

  public static boolean firstCheck(String st) {
    if (st.charAt(0) == '{' || st.charAt(st.length() - 1) == '}') {
      return true;
    }
    return false;
  }

  public static String subObj(String st, int a) {
    int c = 0;
    int i = 0;
    while (a > 0) {
      if (st.charAt(i) == '"') {
        i++;
        while (st.charAt(i) != '"') {
          if (st.charAt(i) == '\\') {
            i++;
          }
          i++;
        }
      }
      if (st.charAt(i) == '{') {
        a--;
      }
      i++;
    }
    int strt = i;
    String st1 = new String();
    while (c != -1) {
      if (st.charAt(i) == '"') {
        i++;
        while (st.charAt(i) != '"') {
          if (st.charAt(i) == '\\') {
            i++;
          }
          i++;
        }
      }

      if (st.charAt(i) == '{') {
        c++;
        if (c == 1) {
          st1 += st.substring(strt, i);
        }
      }

      if (st.charAt(i) == '}') {
        c--;
        if (c == -1) {
          st1 += st.substring(strt, i);
        }
        if (c == 0) {
          strt = i + 1;
        }
      }
      i++;
    }
    return st1;
  }

  public static int objectCounter(String st) {
    int aculadop = 0;
    int aculadclo = 0;
    for (int i = 0; i < st.length(); i++) {
      if (st.charAt(i) == '"') {
        i++;
        while (st.charAt(i) != '"') {
          if (st.charAt(i) == '\\') {
            i++;
          }
          i++;
        }
      }
      if ('{' == st.charAt(i)) {
        aculadop++;
      }
      if ('}' == st.charAt(i)) {
        aculadclo++;
      }
    }
    if (aculadclo == aculadop) {
      return aculadclo;
    }
    return 0;
  }

  public static boolean keyCheck(String in) {
    try {
      int i = 0;
      if (in.charAt(i) == '"') {
        i++;
        while (in.charAt(i) != '"') {
          if (in.charAt(i) == '\\') {
            i++;
            if (in.charAt(i) == '"' || in.charAt(i) == '\\' || in.charAt(i) == '\'') {
              i++;
            } else {
              return false;
            }
          }
          if (in.charAt(i) == ' ' || in.charAt(i) == '}') {
            return false;
          }
          i++;
        }
        if (in.charAt(i - 1) == '"') {
          return false;
        }
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) != ':') {
          return false;
        }
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        switch (in.charAt(i)) {
          case '{':
            i++;
            while (in.charAt(i) == ' ') {
              i++;
            }
            in = in.substring(i, in.length());
            return keyCheck(in);

          case '[':
            i++;
            while (in.charAt(i) == ' ') {
              i++;
            }
            in = in.substring(i, in.length());
            return arrayCheck(in);

          case '"':
            i++;
            in = in.substring(i, in.length());
            return stringCheck(in);
        }
        if (in.charAt(i) == 't' || in.charAt(i) == 'f' || in.charAt(i) == 'n') {
          in = in.substring(i, in.length());
          return booleanCheck(in);
        }
        if (in.charAt(i) == '-'
            || (in.charAt(i) <= 57 && in.charAt(i) >= 48 || in.charAt(i) == '.')) {
          in = in.substring(i, in.length());
          return numCheck(in);
        }
      } else if (in.charAt(i) == '}') {
        if (i == in.length() - 1) {
          return true;
        }
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) != '}') {
            in = in.substring(i, in.length());
            return keyCheck(in);
          } else {
            return false;
          }
        }
        if (in.charAt(i) == '}') {
          in = in.substring(i, in.length());
          return keyCheck(in);
        }
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean arrayCheck(String in) {
    try {
      int i = 0;
      switch (in.charAt(i)) {
        case '{':
          i++;
          in = in.substring(i, in.length());
          return objectCheckArray(in);

        case '"':
          i++;
          in = in.substring(i, in.length());
          return stringCheckArray(in);

        case ']':
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) == ',') {
            i++;
            while (in.charAt(i) == ' ') {
              i++;
            }
            if (in.charAt(i) == '"') {
              in = in.substring(i, in.length());
              return keyCheck(in);
            } else {
              return false;
            }
          }
          if (in.charAt(i) == '}') {
            in = in.substring(i, in.length());
            return keyCheck(in);
          }
      }
      if (in.charAt(i) == 't' || in.charAt(i) == 'f' || in.charAt(i) == 'n') {
        in = in.substring(i, in.length());
        return booleanCheckArray(in);
      }
      if (in.charAt(i) == '-'
          || (in.charAt(i) <= 57 && in.charAt(i) >= 48 || in.charAt(i) == '.')) {
        in = in.substring(i, in.length());
        return numCheckArray(in);
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean stringCheck(String in) {
    try {
      int i = 0;
      while (in.charAt(i) != '"') {
        if (in.charAt(i) == '\\') {
          i++;
          if (in.charAt(i) == '"' || in.charAt(i) == '\\' || in.charAt(i) == '\'') {
            // i++;
          } else {
            return false;
          }
        }
        i++;
      }
      i++;
      while (in.charAt(i) == ' ') {
        i++;
      }
      if (in.charAt(i) == ',') {
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) != '}') {
          in = in.substring(i, in.length());
          return keyCheck(in);
        } else {
          return false;
        }
      }
      if (in.charAt(i) == '}') {
        in = in.substring(i, in.length());
        return keyCheck(in);
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean booleanCheck(String in) {
    try {
      int i = 0;
      if (in.substring(i, i + 4).equals("true") || in.substring(i, i + 4).equals("null")) {
        i += 4;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) != '}') {
            in = in.substring(i, in.length());
            return keyCheck(in);
          }
        }
        if (in.charAt(i) == '}') {
          in = in.substring(i, in.length());
          return keyCheck(in);
        }
      }
      if (in.substring(i, i + 5).equals("false")) {
        i += 5;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) != '}') in = in.substring(i, in.length());
          return keyCheck(in);
        }
        if (in.charAt(i) == '}') {
          in = in.substring(i, in.length());
          return keyCheck(in);
        }
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean numCheck(String in) {
    try {
      int i = 0;
      while (in.charAt(i) != ' ' && in.charAt(i) != ',' && in.charAt(i) != '}') {
        i++;
      }

      String num = in.substring(0, i);
      try {
        long x = Long.parseLong(num);
      } catch (Exception e) {
        try {
          double y = Double.parseDouble(num);
        } catch (Exception e1) {
          return false;
        }
      }
      while (in.charAt(i) == ' ') {
        i++;
      }
      if (in.charAt(i) == ',') {
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) != '}') {
          in = in.substring(i, in.length());
          return keyCheck(in);
        } else {
          return false;
        }
      } else if (in.charAt(i) == '}') {
        in = in.substring(i, in.length());
        return keyCheck(in);
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean objectCheckArray(String in) {
    try {
      int i = 0, c = 0;
      while (c != -1) {
        if (in.charAt(i) == '"') {
          i++;
          while (in.charAt(i) != '"') {
            i++;
            if (in.charAt(i) == '\\') {
              i += 2;
            }
          }
        }
        if (in.charAt(i) == '{') {
          c++;
        }

        if (in.charAt(i) == '}') {
          c--;
        }
        i++;
      }
      String st = in.substring(0, i);
      if (keyCheck(st)) {
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          in = in.substring(i, in.length());
          return arrayCheck(in);
        }
        if (in.charAt(i) == ']') {
          in = in.substring(i, in.length());
          return arrayCheck(in);
        }
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean stringCheckArray(String in) {
    try {
      int i = 0;
      while (in.charAt(i) != '"') {
        if (in.charAt(i) == '\\') {
          i++;
          if (in.charAt(i) == '"' || in.charAt(i) == '\\' || in.charAt(i) == '\'') {
          } else {
            return false;
          }
        }
        i++;
      }
      i++;
      while (in.charAt(i) == ' ') {
        i++;
      }
      if (in.charAt(i) == ',') {
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) != ']') {
          in = in.substring(i, in.length());
          return arrayCheck(in);
        } else {
          return false;
        }
      }
      if (in.charAt(i) == ']') {
        in = in.substring(i, in.length());
        return arrayCheck(in);
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean numCheckArray(String in) {
    try {
      int i = 0;
      while (in.charAt(i) != ' ' && in.charAt(i) != ',' && in.charAt(i) != ']') {
        i++;
      }

      String num = in.substring(0, i);
      try {
        long x = Long.parseLong(num);
      } catch (Exception e) {
        try {
          double y = Double.parseDouble(num);
        } catch (Exception e1) {
          return false;
        }
      }
      while (in.charAt(i) == ' ') {
        i++;
      }
      if (in.charAt(i) == ',') {
        i++;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) != ']') {
          in = in.substring(i, in.length());
          return arrayCheck(in);
        } else {
          return false;
        }
      }
      if (in.charAt(i) == ']') {
        in = in.substring(i, in.length());
        return arrayCheck(in);
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static boolean booleanCheckArray(String in) {
    try {
      int i = 0;
      if (in.substring(i, i + 4).equals("true") || in.substring(i, i + 4).equals("null")) {
        i += 4;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) != ']') {
            in = in.substring(i, in.length());
            return arrayCheck(in);
          } else {
            return false;
          }
        }
        if (in.charAt(i) == ']') {
          in = in.substring(i);
          return arrayCheck(in);
        }
      }

      if (in.substring(i, i + 5).equals("false")) {
        i += 5;
        while (in.charAt(i) == ' ') {
          i++;
        }
        if (in.charAt(i) == ',') {
          i++;
          while (in.charAt(i) == ' ') {
            i++;
          }
          if (in.charAt(i) != ']') {
            in = in.substring(i, in.length());
            return arrayCheck(in);
          } else {
            return false;
          }
        }
        if (in.charAt(i) == ']') {
          in = in.substring(i);
          return arrayCheck(in);
        }
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }

  public static int JSONcounter(String in) {
    int i = in.length() - 1;
    int value = 0;
    int answer = 0;
    int k = 0;
    String[] keys = new String[200];
    String key;
    while (i > 0) {
      while (in.charAt(i) != ':' && in.charAt(i) != ']' && in.charAt(i) != '"') {
        i--;
      }
      if (in.charAt(i) == '"') {
        i--;
        if (in.charAt(i) == '"' && in.charAt(i - 1) == '\\') {
          i -= 2;
        }
        while (in.charAt(i) != '"') {
          if (in.charAt(i - 1) == '"' && in.charAt(i - 2) == '\\') {
            i -= 2;
          }
          i--;
        }
        i--;
      }
      if (in.charAt(i) == ':') {
        value++;
        i -= 2;
      }
      if (in.charAt(i) == ']') {
        i--;
        value++;
        while (in.charAt(i) != '[') {
          if (in.charAt(i) == '"') {
            i--;
            if (in.charAt(i - 1) == '\\') {
              i -= 2;
            }
            while (in.charAt(i) != '"') {

              if (in.charAt(i - 2) == '\\') {
                i--;
              }
              i--;
            }
          }
          if (in.charAt(i) == ',') {
            value++;
          }
          i--;
        }
        i--;
        if (in.charAt(i) == ':') {
          value++;
          i -= 2;
        }
      }
      int j = i + 1;
      while (in.charAt(i) != '"') {
        i--;
      }
      key = in.substring(i, j + 1);
      if (k == 0) {
        i -= 2;
        keys[k] = key;
        answer += value;
        value = 0;
        k++;
        keys[k] = "0";
      } else {
        for (int a = 0; a < k; a++) {
          if (keys[a].equals(key)) {
            value = 0;
            break;
          }
          if (a == k - 1 && !keys[a].equals(key)) {
            answer += value;
            value = 0;
            keys[k] = key;
            k++;
            i--;
            break;
          }
        }
      }
    }
    return answer;
  }
}

class MyList implements List {

  private Object[] O = new Object[1000];
  private int index = 0;
  private int size = 0;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public boolean contains(Object o) {
    for (int i = 0; i < size; i++) {
      if (O[i].equals(o)) {
        return true;
      }
    }
    return false;
  }

  public Iterator iterator() {
    return null;
  }

  public Object[] toArray() {
    return O;
  }

  public Object[] toArray(Object[] a) {
    if (size < a.length) {
      for (int i = 0; i < size; i++) {
        a[i] = O[i];
      }
      for (int i = size; i < a.length; i++) {
        a[i] = null;
      }
    } else if (size == a.length) {
      for (int i = 0; i < size; i++) {
        a[i] = O[i];
      }
    } else {
      System.out.println("Not Possible");
    }
    return a;
  }

  @Override
  public boolean add(Object e) {
    this.O[index] = e;
    index++;
    size++;
    return true;
  }

  public boolean remove(Object o) {
    int i = 0;
    for (; i < size; i++) {
      if (O[i].equals(o)) {
        break;
      }
    }
    if (i != size) {
      O[i] = null;
      for (int j = i; j < size; j++) {
        O[j] = O[j + 1];
      }
      index--;
      size--;
      return true;
    }
    return false;
  }

  public boolean containsAll(Collection c) {
    Object[] x = c.toArray();
    boolean flag = false;
    int counter = 0;
    if (size >= c.size()) {
      for (int i = 0; i < c.size(); i++) {
        for (int j = 0; j < size; j++) {
          if (x[i].equals(O[j])) {
            counter++;
          }
        }
      }
      if (counter == c.size()) {
        flag = true;
      }
    } else {
      System.out.println("Not Possible");
    }
    return flag;
  }

  public boolean addAll(Collection c) {
    boolean changed = false;
    Object[] x = c.toArray();
    for (int i = 0; i < x.length; i++) {
      if (true) {
        this.add(x[i]);
        changed = true;
      }
    }
    return changed;
  }

  public boolean addAll(int index, Collection c) {
    boolean flag = false;
    int save = this.index;
    int counter = 0;
    Object[] x = c.toArray();
    counter = c.size();
    for (int i = size - 1; i >= index; i--) {
      O[size + counter] = O[size];
    }
    this.index = index;
    for (int i = 0; i < c.size(); i++) {
      if (true) {
        this.add(x[i]);
      }
    }
    this.index = save + counter;
    return flag;
  }

  public boolean removeAll(Collection c) {
    boolean flag = false;
    for (Object object : O) {
      if (c.contains(object)) {
        this.remove(object);
        flag = true;
      }
    }
    return flag;
  }

  public boolean retainAll(Collection c) {
    boolean flag = false;
    for (Object object : O) {
      if (!c.contains(object)) {
        this.remove(object);
        flag = true;
      }
    }
    return flag;
  }

  public void clear() {
    for (Object object : O) {
      remove(object);
    }
    size = 0;
    index = 0;
  }

  public Object get(int index) {
    return this.O[index];
  }

  public Object set(int index, Object element) {
    Object x = this.O[index];
    this.O[index] = element;
    return x;
  }

  public void add(int index, Object element) {
    for (int i = size; i > index; i--) {
      this.O[i + 1] = this.O[i];
      this.O[index] = element;
    }
    this.index++;
    size++;
  }

  public Object remove(int index) {
    Object x = this.O[index];
    for (int i = index; i < size; i++) {
      this.O[i] = this.O[i + 1];
    }
    this.index--;
    size--;
    return x;
  }

  public int indexOf(Object o) {
    int i = 0;
    for (; i < size; i++) {
      if (O[i].equals(o)) {
        break;
      }
    }
    if (i == size) {
      return -1;
    } else {
      return i;
    }
  }

  public int lastIndexOf(Object o) {
    int i = size - 1;
    for (; i >= 0; i--) {
      if (O[i].equals(o)) {
        break;
      }
    }
    return i;
  }

  public ListIterator listIterator() {
    // TODO Auto-generated method stub
    return null;
  }

  public ListIterator listIterator(int index) {
    // TODO Auto-generated method stub
    return null;
  }

  public MyList subList(int fromIndex, int toIndex) {
    int x = toIndex - fromIndex;
    MyList X = new MyList();
    X.size = x;
    X.index = x;
    for (int i = 0; i < x; i++) {
      X.O[i] = this.O[i + fromIndex];
    }
    return X;
  }
}

class MyMap implements Map {

  private Object[] k = new Object[1000];
  private Object[] v = new Object[1000];
  private int index = 0;
  private int size = 0;

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    int i = 0;
    for (; i < size; i++) {
      if (k[i].equals(key)) {
        break;
      }
    }
    return i < size;
  }

  @Override
  public boolean containsValue(Object value) {
    int i = 0;
    for (; i < size; i++) {
      if (v[i].equals(value)) {
        break;
      }
    }
    return i < size;
  }

  @Override
  public Object get(Object key) {
    int i = 0;
    for (; i < size; i++) {
      if (k[i].equals(key)) {
        break;
      }
    }
    return v[i];
  }

  @Override
  public Object put(Object key, Object value) {
    if (!containsKey(key)) {
      k[index] = key;
      v[index] = value;
      index++;
      size++;
      return null;
    } else {
      Object x = get(key);
      int i = 0;
      for (; i < size; i++) {
        if (k[i].equals(key)) {
          break;
        }
      }
      v[i] = value;
      return x;
    }
  }

  @Override
  public Object remove(Object key) {
    if (!containsKey(key)) {
      return null;
    } else {
      int i = 0;
      for (; i < size; i++) {
        if (k[i].equals(key)) {
          break;
        }
      }
      Object x = v[i];
      for (int j = i; i < size; i++) {
        v[j] = v[j + 1];
        k[j] = k[j + 1];
      }
      size--;
      index--;
      return x;
    }
  }

  @Override
  public void putAll(Map m) {
    MyMap x = (MyMap) m;
    for (int i = 0; i < m.size(); i++) {
      this.put(x.k[i], x.v[i]);
    }
  }

  @Override
  public void clear() {
    for (int i = 0; i < size; i++) {
      v[i] = null;
      k[i] = null;
    }
    index = 0;
    size = 0;
  }

  @Override
  public Set keySet() {
    MySet x = new MySet();
    for (int i = 0; i < this.size; i++) {
      x.add(k[i]);
    }
    return x;
  }

  @Override
  public Collection values() {
    MyList x = new MyList();
    for (int i = 0; i < size; i++) {
      x.add(v[i]);
    }
    return x;
  }

  @Override
  public Set entrySet() { // I Have No Clue

    return null;
  }
}

class MySet implements Set {
  private int size;
  private int index;
  private Object[] O = new Object[1000];

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public boolean contains(Object o) {
    for (int i = 0; i < size; i++) {
      if (O[i].equals(o)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Iterator iterator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Object[] toArray() {
    return O;
  }

  @Override
  public Object[] toArray(Object[] a) {
    if (size < a.length) {
      for (int i = 0; i < size; i++) {
        a[i] = O[i];
      }
      for (int i = size; i < a.length; i++) {
        a[i] = null;
      }
    } else if (size == a.length) {
      for (int i = 0; i < size; i++) {
        a[i] = O[i];
      }
    } else {
      System.out.println("Not Possible");
    }
    return a;
  }

  @Override
  public boolean add(Object e) {
    int i = 0;
    for (; i < size; i++) {
      if (O[i].equals(e)) {
        break;
      }
    }
    if (i == size) {
      O[index] = e;
      size++;
      index++;
      return true;
    }
    return false;
  }

  @Override
  public boolean remove(Object o) {
    int i = 0;
    for (; i < size; i++) {
      if (O[i].equals(o)) {
        break;
      }
    }
    if (i != size) {
      O[i] = null;
      for (int j = i; j < size; j++) {
        O[i] = O[i + 1];
      }
      index--;
      size--;
      return true;
    }
    return false;
  }

  @Override
  public boolean containsAll(Collection c) {
    Object[] x = c.toArray();
    boolean flag = false;
    int counter = 0;
    if (size >= c.size()) {
      for (int i = 0; i < c.size(); i++) {
        for (int j = 0; j < size; j++) {
          if (x[i].equals(O[j])) {
            counter++;
          }
        }
      }
      if (counter == c.size()) {
        flag = true;
      }
    } else {
      flag = false;
      System.out.println("Not Possible");
    }
    return flag;
  }

  @Override
  public boolean addAll(Collection c) {
    boolean setChanged = false;
    Object[] x = c.toArray();
    for (int i = 0; i < c.size(); ) {
      if (this.contains(x[i])) {
        i++;
      } else {
        this.add(x[i]);
        i++;
        setChanged = true;
      }
    }
    return setChanged;
  }

  @Override
  public boolean retainAll(Collection c) {
    boolean setChanged = false;
    for (Object object : this.O) {
      if (!c.contains(object)) {
        this.remove(object);
        setChanged = true;
      }
    }
    return setChanged;
  }

  @Override
  public boolean removeAll(Collection c) {
    boolean setChanged = false;
    for (Object object : this.O) {
      if (c.contains(object)) {
        this.remove(object);
        setChanged = true;
      }
    }
    return setChanged;
  }

  @Override
  public void clear() {
    for (int i = size - 1; i >= 0; i--) {
      remove(O[i]);
    }
    index = 0;
    size = 0;
  }
}
