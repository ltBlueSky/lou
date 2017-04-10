/*
 * *****************************************************************************************
 * Copyright  (c) 2016 Lou
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * *****************************************************************************************
 */

package com.lyloou.demo.view;

/**
 * Author:    Lou
 * Version:   V1.0
 * Date:      2016.11.18 14:28
 * <p>
 * Description:
 */
public interface IUserView {
    int getId();
    String getFirstName();
    String getLastName();
    void setFirstName(String firstName);
    void setLastName(String lastName);
}