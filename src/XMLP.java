import java.util.Scanner;

/**
 * A standalone exercise for parsing and validating basic XML strings from standard input.
 * It manually checks tags and values without external XML libraries.
 */
public class XMLP {

  public static void main(String[] args) {
    // Read XML string from standard input
    Scanner input = new Scanner(System.in);
    String stmain = "";
    boolean flag = true;
    String first = input.next();
    stmain += first;
    for (int i = 0; i < first.length(); i++) {
      if (first.charAt(i) == '>') {
        if (first.length() != i + 1) {
          flag = false;
        }
      }
    }
    while (flag) {
      String st = input.next();
      stmain += st;
      if (st.equals(first.charAt(0) + "/" + first.substring(1))) {
        flag = false;
      }
    }
    // Validate if the XML tags and values are correct
    if (checkAllValues(stmain)) {
      System.out.println("Values Are Correct");
    } else {
      System.out.println(0);
    }

    // Additional tags check
    String stmain2 = "";
    if (true) {
      stmain = stmain.replaceAll(" ", "");
      stmain += " ";
      int start = 0;
      for (; start < stmain.length() - 3; start++) {
        int end = start + 3;
        for (; end < stmain.length(); end++) {
          if (startTag(stmain.substring(start, end)) || endTag(stmain.substring(start, end))) {
            stmain2 += stmain.substring(start, end);
          }
        }
      }
    }
    input.close();
  }

  public static boolean startTag(String st) {
    int bigger = 0, smaller = 0;
    for (int i = 0; i < st.length(); i++) {
      if (st.charAt(i) == '<') {
        smaller++;
      }
      if (st.charAt(i) == '>') {
        bigger++;
      }
    }
    if (st.charAt(0) == '<' && st.charAt(st.length() - 1) == '>' && smaller == 1 && bigger == 1) {
      return true;
    } else return false;
  }

  public static boolean endTag(String st) {
    int bigger = 0, smaller = 0;
    for (int i = 0; i < st.length(); i++) {
      if (st.charAt(i) == '<') {
        smaller++;
      }
      if (st.charAt(i) == '>') {
        bigger++;
      }
    }
    if (st.charAt(1) == '/'
        && st.charAt(0) == '<'
        && st.charAt(st.length() - 1) == '>'
        && smaller == 1
        && bigger == 1) {
      return true;
    } else return false;
  }

  public static int numOfTags(String st) {
    int counter = 0;
    for (int i = 0; i < st.length(); i++) {
      if (st.charAt(i) == '<') {
        counter++;
      }
    }
    return counter;
  }

  public static boolean checkAllValues(String st) {
    int start = 0;
    for (; start < st.length() - 1; start++) {
      int end = start + 1;
      for (; end < st.length(); end++) {
        if (isAllValue(st.substring(start, end))) {
          String sta = st.substring(start, end);
          if (isValueNoneString(sta) || isValueString(sta)) {
            continue;
          } else {
            return false;
          }
        } else {
          continue;
        }
      }
    }
    return true;
  }

  public static boolean isAllValue(String st) {
    if (!(st.charAt(0) == '>' && st.charAt(st.length() - 1) == '<')) {
      return false;
    }
    st = st.substring(1, st.length() - 1);
    int counter = 0;
    for (int i = 0; i < st.length(); i++) {
      if (st.charAt(i) == '<' || st.charAt(i) == '>') {
        counter++;
      }
    }
    return counter == 0;
  }

  public static boolean isValueNoneString(String st) {
    st = st.substring(1, st.length() - 1);
    boolean flag = true;
    if (st.equals("false") || st.equals("true") || st.equals("")) {
      return true;
    }
    try {
      long x = Long.parseLong(st);
    } catch (Exception e) {
      try {
        double d = Double.parseDouble(st);
      } catch (Exception e1) {
        flag = false;
      }
    }
    return flag;
  }

  public static boolean isValueString(String st) {
    st = st.substring(1, st.length() - 1);
    if (st.charAt(0) != '"' || st.charAt(st.length() - 1) != '"') {
      return false;
    }
    for (int i = 1; i < st.length() - 1; i++) {
      if ((st.charAt(i) == '"' || st.charAt(i) == '>' || st.charAt(i) == '<')
          && st.charAt(i - 1) != '\\') {
        return false;
      }
    }
    return true;
  }
}
