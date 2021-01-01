/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.samples.bank.persistence;


import com.tangosol.net.cache.AbstractCacheLoader;
import com.tangosol.util.Base;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Collections;


/**
 * @author Aleksandar Seovic  2010.02.02
 */
public class ExchangeRateRestCacheLoader
        extends AbstractCacheLoader
    {
    public Object load(Object key)
        {
        Map<String, String> params =
                Collections.singletonMap("instrument", (String) key);

        RestTemplate client = new RestTemplate();
        String rate = client.getForObject(m_url, String.class, params);

        return new BigDecimal(rate);
        }

    public void setUrl(String url)
        {
        m_url = url;
        }

    private String m_url;
    }
