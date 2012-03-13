/*
 * Copyright (c) 2010 Steve Reed
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package algorithms.search;

import java.util.Iterator;

/**
 * An object that finds a string in others.
 */
public interface StringFinder {

  int indexIn(CharSequence string);

  int indexIn(CharSequence string, int startIndex);

  int countOccurencesIn(CharSequence string);

  Iterator<Integer> allMatches(CharSequence text, int startIndex);
}
