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



