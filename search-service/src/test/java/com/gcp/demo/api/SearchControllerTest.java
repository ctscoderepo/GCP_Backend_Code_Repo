package com.gcp.demo.api;

import com.gcp.demo.Constants;
import com.gcp.demo.exceptions.InvalidInputException;
import com.gcp.demo.model.Facet;
import com.gcp.demo.model.Product;
import com.gcp.demo.model.SearchResult;
import com.gcp.demo.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchControllerTest {
    @Mock
    private SearchService mockService;
    @InjectMocks
    private SearchController controller;

    private static LinkedHashMap<String, Long> getLinkedMap(String key, Long val) {
        LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        map.put(key, val);
        return map;
    }
    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnKeyWordSearch() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(getLinkedMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(getLinkedMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByKeyword(eq("a"), any(), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeKeywordSearch("a", null, "PRICE_HIGH_2_LOW");
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordContainsSTAR() throws Exception {
        when(mockService.searchByKeyword(anyString(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch("a*", null, "PRICE_LOW_2_HIGH");
        verify(mockService, times(1)).searchByKeyword("a*", null, Constants.SortBy.PRICE_LOW_2_HIGH);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordNull() throws Exception {
        when(mockService.searchByKeyword(any(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch(null, null, null);
        verify(mockService, times(1)).searchByKeyword(null, null, null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnKeyWordSearchWhenKeywordEmpty() throws Exception {
        when(mockService.searchByKeyword(anyString(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeKeywordSearch("", null, null);
        verify(mockService, times(1)).searchByKeyword("a*", null, null);
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1AndCat2() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(getLinkedMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(getLinkedMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory(eq("a"), eq("b"), any(), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", "b",null, null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1Only() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(getLinkedMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(getLinkedMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory(eq("a"), any(), any(),any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", null, null, null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test
    public void shouldReturnSearchResponseWithTotalAndFacetsOnGuidedSearchWithCat1AndCat2Empty() throws Exception {
        SearchResult mockResponse = new SearchResult();
        mockResponse.setTotal(1);
        mockResponse.setProducts(Collections.singletonList(
                Product.builder().brand("AA").id("1")
                        .build()));
        mockResponse.setFacets(Arrays.asList(
                Facet.builder().name("facet1").buckets(getLinkedMap("bucket1", 1L)).build(),
                Facet.builder().name("facet2").buckets(getLinkedMap("bucket2", 1L)).build()
        ));
        when(mockService.searchByCategory(eq("a"), eq(""), any(), any())).thenReturn(mockResponse);
        SearchResult actual = controller.executeGuidedSearch("a", "", null, null);
        assertThat(actual.getTotal(), equalTo(1L));
        assertThat(actual.getProducts().get(0).getId(), equalTo("1"));
        assertThat(actual.getFacets().get(0).getName(), equalTo("facet1"));
        assertThat(actual.getFacets().get(0).getBuckets(), hasEntry(is("bucket1"), is(1L)));
        assertThat(actual.getFacets().get(1).getName(), equalTo("facet2"));
        assertThat(actual.getFacets().get(1).getBuckets(), hasEntry(is("bucket2"), is(1L)));
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Empty() throws Exception {
        when(mockService.searchByCategory(eq(""), anyString(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch("", "X", null, "PRICE_HIGH_2_LOW");
        verify(mockService, times(1)).searchByCategory("", "X", null, Constants.SortBy.PRICE_HIGH_2_LOW);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategory1Null() throws Exception {
        when(mockService.searchByCategory(any(), anyString(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch(null, "X", null, null);
        verify(mockService, times(1)).searchByCategory(null, "X", null, null);
    }

    @Test(expected = InvalidInputException.class)
    public void shouldThrowInvalidInputExceptionOnGuidedSearchWhenCategoriesNull() throws Exception {
        when(mockService.searchByCategory(any(), any(), any(), any())).thenThrow(new InvalidInputException("Expected exception from testcase"));
        controller.executeGuidedSearch(null, null,null, null);
        verify(mockService, times(1)).searchByCategory(null, null, null, null);
    }
}