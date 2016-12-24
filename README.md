AutoPreferences
=========================
[![Bintray](https://img.shields.io/bintray/v/kakai248/maven/autopreferences.svg)](https://bintray.com/kakai248/maven/autopreferences/view)

This library generates a preferences class to manage preferences
in Android based on annotations.

Installation
------
This library requires Java 8 to run the annotation processor.

You can check the lastest version number at the top of this README.

```groovy
compile 'com.kakai.android.autopreferences:library:${LATEST_VERSION}'
annotationProcessor 'com.kakai.android.autopreferences:compiler:${LATEST_VERSION}'
```

**Backup (optional):**

```groovy
compile 'com.kakai.android.autopreferences:backup:${LATEST_VERSION}'
```

Usage
-------
```java
@AutoPreferences(annotate = true)
public abstract class AbstractPreferencesManager {

    public static final int GROUP1 = 1;

    @Preference(stringRes = R.string.test_boolean, remove = true, contains = true, tag = GROUP1)
    boolean testBoolean = true;
}
```

You need to create a class and annotate it with `@AutoPreferences`. If you also use `annotate = true`,
it will annotate each getter and setter generated with `@PreferenceGetter` and `@PreferenceSetter`
(this is needed to use the backup feature).

If you prefix the class name with `Abstract`, like in the example, the processor will generate a class
with the same name except the prefix. If it doesn't start with `Abstract`, it will have the same name
and the processor will add the prefix `Auto`.

| Annotated class name | Generated class name |
|---|---|
| AbstractPreferencesManager | PreferencesManager |
| PreferencesManager | AutoPreferencesManager |

To define a preferences, annotate a field with `@Preference`. Give the preference a key (using a string
resource, to be compatible with PreferenceFragments) using `stringRes = R.string.test_boolean`.

You can also pass `remove = true` and `contains = true` to generate a remove and a contains method for
that preference.

Finally you can define an int (`tag = 10`) to be used as tag. This tag will be in the `@PreferenceGetter`
and `@PreferenceSetter` annotations.

The value that you assign to the field will be the default value of the preference. It is recommended
that you assign a protected visibility to the field to avoid accidental access.

The previous example will generate the following class:

```java
public class PreferencesManager extends AbstractPreferencesManager {
  private Context context;

  private PreferencesHelper helper;

  public PreferencesManager(Context context) {
    super();
    this.context = context;
    this.helper = PreferencesHelper.with(context);
  }

  @PreferenceGetter(
      stringRes = 2131099689,
      tag = 1
  )
  public boolean isTestBoolean() {
    return helper.getBoolean(context.getString(2131099689), testBoolean);
  }

  @PreferenceSetter(
      stringRes = 2131099689,
      tag = 1
  )
  public void setTestBoolean(boolean testBoolean) {
    helper.putBoolean(context.getString(2131099689), testBoolean);
  }

  public void removeTestBoolean() {
    helper.remove(context.getString(2131099689));
  }

  public boolean hasTestBoolean() {
    return helper.contains(context.getString(2131099689));
  }
}
```

The library support all the types that the Android SharedPreferences support plus `Enum`.

| Supported types |
|---|
| Boolean |
| String |
| Int |
| Float |
| Long |
| StringSet |
| Enum |

Backup (optional)
------
It is also included a backup feature. As said before, you need to use `@AutoPreferences(annotate = true)`.
There is a `BackupHelper` that provides methods to get/set the values of the generated class. You can
retrieve or store the values using a `Map<String, Object>`, a `Json` string or a `File` (saves as `Json`).

You can pass the tags to the read/backup methods of the `BackupHelper` to only save the fields that are
annotated with those tags.

It uses Jackson to create/read the `Json`. Ideally it would support any `Json` parser.

Example
------
See the included sample app.

License
-------

    Copyright 2016 Ricardo Carrapiço

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
