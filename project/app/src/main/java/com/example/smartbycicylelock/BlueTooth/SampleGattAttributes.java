/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.smartbycicylelock.BlueTooth;

import java.util.HashMap;

/**
 * 이 클래스는 데모 목적을 위한 표준 GATT의 속성 작은 서브셋을 포함한다.
 */
public class SampleGattAttributes {

    private static HashMap<String, String> attributes = new HashMap();
    public static String DISPLAY_RAITING_BATTERY_PERCENT = "0000180f-0000-1000-8000-00805f9b34fb";
    public static String BATTERY_CHARACTERISTIC_SERVICE = "00002a19-0000-1000-8000-00805f9b34fb";

    static {

        // Batery Rate Service UUID
        attributes.put("0000180f-0000-1000-8000-00805f9b34fb", "Battery Rate Service");
        // Sample Characteristics.
        attributes.put("00002a19-0000-1000-8000-00805f9b34fb", "Display Battery Percent");

    }

    // 특성의 Hash 테이블 이름값 보여주기
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
