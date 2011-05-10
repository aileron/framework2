/**
 * Copyright (C) 2008 aileron.cc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.aileron.commons.util;

import static java.util.Calendar.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cc.aileron.generic.util.XRange;

import com.google.inject.Singleton;

/**
 * WeeklyCalendarSqlLogic 実装
 * 
 * @author Aileron
 * 
 */
@Singleton
public class WeeklyCalendarSql
{
    /**
     * @param date
     * @return sql
     */
    public String createSql(final Date date)
    {
        final StringBuilder builder = new StringBuilder(
                "CREATE TEMPORARY TABLE calendar1 (day date primary key);\n");

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int from = calendar.getActualMinimum(DAY_OF_WEEK);
        final int to = calendar.getActualMaximum(DAY_OF_WEEK);
        for (final int i : XRange.fromTo(from, to))
        {
            calendar.set(DAY_OF_WEEK, i);

            final String day = format.format(calendar.getTime());
            builder.append("INSERT INTO calendar1 (day) VALUES ('")
                .append(day)
                .append("');")
                .append("\n");
        }

        return builder.toString();
    }

    /**
     * constractor set format
     */
    public WeeklyCalendarSql()
    {
        format = new SimpleDateFormat("yyyy-MM-dd");
    }

    /**
     * 日付フォーマット
     */
    private final SimpleDateFormat format;
}