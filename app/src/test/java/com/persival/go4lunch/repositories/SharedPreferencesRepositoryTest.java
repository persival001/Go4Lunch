package com.persival.go4lunch.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.persival.go4lunch.data.shared_prefs.SharedPreferencesRepository;
import com.persival.go4lunch.domain.location.model.LocationEntity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SharedPreferencesRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application mockApplication;
    @Mock
    private SharedPreferences mockSharedPreferences;
    @Mock
    private SharedPreferences.Editor mockEditor;

    private SharedPreferencesRepository sharedPreferencesRepository;

    @Before
    public void setup() {
        when(mockApplication.getSharedPreferences(eq("SharedPrefs"), eq(Context.MODE_PRIVATE))).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        sharedPreferencesRepository = new SharedPreferencesRepository(mockApplication);
    }

    @Test
    public void testGetLocation_Success() {
        // Given
        float latitude = 48.85f;
        float longitude = 2.35f;
        when(mockSharedPreferences.getFloat(eq(SharedPreferencesRepository.KEY_LATITUDE), eq(-1f))).thenReturn(latitude);
        when(mockSharedPreferences.getFloat(eq(SharedPreferencesRepository.KEY_LONGITUDE), eq(-1f))).thenReturn(longitude);

        // When
        LocationEntity location = sharedPreferencesRepository.getLocation();

        // Then
        verify(mockSharedPreferences).getFloat(eq(SharedPreferencesRepository.KEY_LATITUDE), eq(-1f));
        verify(mockSharedPreferences).getFloat(eq(SharedPreferencesRepository.KEY_LONGITUDE), eq(-1f));
        assertEquals(latitude, location.getLatitude(), 0);
        assertEquals(longitude, location.getLongitude(), 0);
    }

    @Test
    public void testGetLocation_Null() {
        // Given
        when(mockSharedPreferences.getFloat(eq(SharedPreferencesRepository.KEY_LATITUDE), eq(-1f))).thenReturn(-1f);
        when(mockSharedPreferences.getFloat(eq(SharedPreferencesRepository.KEY_LONGITUDE), eq(-1f))).thenReturn(-1f);

        // When
        LocationEntity location = sharedPreferencesRepository.getLocation();

        // Then
        verify(mockSharedPreferences).getFloat(eq(SharedPreferencesRepository.KEY_LATITUDE), eq(-1f));
        verify(mockSharedPreferences).getFloat(eq(SharedPreferencesRepository.KEY_LONGITUDE), eq(-1f));
        assertNull(location);
    }

    @Test
    public void testSaveLocation() {
        // Given
        LocationEntity location = new LocationEntity(48.85, 2.35);
        when(mockEditor.putFloat(anyString(), anyFloat())).thenReturn(mockEditor);

        // When
        sharedPreferencesRepository.saveLocation(location);

        // Then
        verify(mockEditor).putFloat(eq(SharedPreferencesRepository.KEY_LATITUDE), eq(48.85f));
        verify(mockEditor).putFloat(eq(SharedPreferencesRepository.KEY_LONGITUDE), eq(2.35f));
        verify(mockEditor).apply();
    }


    @Test
    public void testGetWorkId() {
        // Given
        String workId = "workId";
        when(mockSharedPreferences.getString(eq(SharedPreferencesRepository.KEY_WORK_ID), isNull())).thenReturn(workId);

        // When
        String result = sharedPreferencesRepository.getWorkId();

        // Then
        verify(mockSharedPreferences).getString(eq(SharedPreferencesRepository.KEY_WORK_ID), isNull());
        assertEquals(workId, result);
    }

    @Test
    public void testSaveWorkId() {
        // Given
        String workId = "work123";
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);

        // When
        sharedPreferencesRepository.saveWorkId(workId);

        // Then
        verify(mockEditor).putString(eq(SharedPreferencesRepository.KEY_WORK_ID), eq("work123"));
        verify(mockEditor).apply();
    }

    @Test
    public void testRemoveWorkId() {
        // Given
        when(mockEditor.remove(anyString())).thenReturn(mockEditor);

        // When
        sharedPreferencesRepository.removeWorkId();

        // Then
        verify(mockEditor).remove(eq(SharedPreferencesRepository.KEY_WORK_ID));
        verify(mockEditor).apply();
    }

}