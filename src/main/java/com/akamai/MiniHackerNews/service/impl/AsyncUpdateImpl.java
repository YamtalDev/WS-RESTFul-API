/******************************************************************************
MIT License

Copyright (c) 2023 Tal Aharon

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

******************************************************************************/

package com.akamai.MiniHackerNews.service.impl;

import java.util.List;
import com.akamai.MiniHackerNews.service.AsyncUpdate;
import com.akamai.MiniHackerNews.schema.NewsPostSchema;
import org.springframework.core.task.AsyncTaskExecutor;
import com.akamai.MiniHackerNews.repository.NewsPostRepository;

public class AsyncUpdateImpl implements AsyncUpdate
{
    private final NewsPostRepository newsPostRepository;
    private final AsyncTaskExecutor asyncTaskExecutor;

    public AsyncUpdateImpl(NewsPostRepository newsPostRepository, AsyncTaskExecutor asyncTaskExecutor)
    {
        this.newsPostRepository = newsPostRepository;
        this.asyncTaskExecutor = asyncTaskExecutor;
    }

    @Override
    public void updateRanksAsync()
    {
        asyncTaskExecutor.submit(() ->
        {
            List<NewsPostSchema> allPosts = newsPostRepository.findAll();
            for(NewsPostSchema post : allPosts)
            {
                post.updateRank();
            }
        });
    }

    @Override
    public void updateTimeElapsedAsync()
    {
        asyncTaskExecutor.submit(() ->
        {
            List<NewsPostSchema> allPosts = newsPostRepository.findAll();
            for(NewsPostSchema post : allPosts)
            {
                post.updateTimeElapsed();
            }
        });
    }
}
