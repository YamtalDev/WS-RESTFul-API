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
package com.akamai.MiniHackerNews.controller;

/* Internal API */
import com.akamai.MiniHackerNews.service.*;
import com.akamai.MiniHackerNews.schema.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CacheConfig;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**************************************************************************
* @description          : Controller layer crud Implementation for the api.
* @NewsPostRequestDTO   : DTO representation of the client post, put requests structure.
* @NewsPostResponseDTO  : DTO to represent the structure of the response to the client side.
* @NewsUpdateRequestDTO : DTO representation of the client patch request structure.
**************************************************************************/

@Validated
@RestController
@RequestMapping("/api/news")
@CacheConfig(cacheNames = "anato")
public class NewsPostController
{
    private NewsPostService newsService;

    public NewsPostController(NewsPostService newsService)
    {
        this.newsService = newsService;
    }

    /**************************************************************************
     * @description : Save a new new post endpoint.
    **************************************************************************/
    @PostMapping("")
    public ResponseEntity<NewsPostResponseDTO> saveNewsPost
    (@Validated @RequestBody NewsPostRequestDTO newsPost)
    {
        return new ResponseEntity<NewsPostResponseDTO>
        (newsService.saveNewPost(newsPost), HttpStatus.CREATED);
    }
    
    /**************************************************************************
     * @description : Retrieve a news post by its ID endpoint.
    **************************************************************************/
    @GetMapping("/{postId}")
    @Cacheable(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> getPostById
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.getPostById(postId), HttpStatus.FOUND));
    }

    /**************************************************************************
     * @description : Update a post by its ID endpoint.
    **************************************************************************/
    @PutMapping("/{postId}")
    @CachePut(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> updatePost
    (@Validated @RequestBody NewsPostRequestDTO updatedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.updatePost(updatedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Delete a post by its ID endpoint.
    **************************************************************************/
    @DeleteMapping("/{postId}")
    @CacheEvict(cacheNames = "anato", key = "#postId")
    public ResponseEntity<String> deletePost
    (@Validated @PathVariable("postId") Long postId)
    {
        newsService.deletePost(postId);
        return (new ResponseEntity<String>("Post deleted", HttpStatus.OK));
    }

    /**************************************************************************
     * @description : Change a post by its ID endpoint.
    **************************************************************************/
    @PatchMapping("/{postId}")
    @CachePut(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> changePost
    (@Validated @RequestBody NewsUpdateRequestDTO changedPost, @Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.changePost(changedPost, postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description    : Retrieve all news posts endpoint.
     * @param pageable : The pagination information.
     * @return         : ResponseEntity with a Page of news posts.
    **************************************************************************/
    @GetMapping("")
    public ResponseEntity<Page<NewsPostResponseDTO>> getAllPosts(Pageable pageable)
    {
        Page<NewsPostResponseDTO> newsPosts = newsService.getAllPosts(pageable);
        return (new ResponseEntity<Page<NewsPostResponseDTO>>(newsPosts, HttpStatus.OK));
    }
    
    /**************************************************************************
     * @description    : Retrieve the top news posts by rank endpoint;
     * @param pageable : The pagination information.
     * @return         : ResponseEntity with a Page of the top news posts.
     **************************************************************************/
    @Cacheable("anato")
    @GetMapping("/top-posts")
    public ResponseEntity<Page<NewsPostResponseDTO>> getTopPostsByRank(Pageable pageable)
    {
        pageable = PageRequest.of(pageable.getPageNumber(), 30);
        Page<NewsPostResponseDTO> newsPosts = newsService.getPostsByRankDesc(pageable);
        return new ResponseEntity<>(newsPosts, HttpStatus.OK);
    }

    /**************************************************************************
     * @description 
    **************************************************************************/
    @PatchMapping("/{postId}/upvote")
    @CachePut(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> upvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.upvotePost(postId), HttpStatus.OK));
    }

    /**************************************************************************
     * @description 
    **************************************************************************/
    @PatchMapping("/{postId}/downvote")
    @CachePut(cacheNames = "anato", key = "#postId")
    public ResponseEntity<NewsPostResponseDTO> downvotePost
    (@Validated @PathVariable("postId") Long postId)
    {
        return (new ResponseEntity<NewsPostResponseDTO>
        (newsService.downvotePost(postId), HttpStatus.OK));
    }
}
