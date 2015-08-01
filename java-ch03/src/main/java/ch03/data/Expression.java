package ch03.data;

import ch03.Context;


public interface Expression {

  TypedValue get(Context context);
  void set(Context context, TypedValue value);
}
