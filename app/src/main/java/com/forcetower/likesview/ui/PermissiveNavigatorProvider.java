package com.forcetower.likesview.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.NavigatorProvider;

public class PermissiveNavigatorProvider extends NavigatorProvider {
        /**
         * A Navigator that only parses the {@link NavDestination} attributes.
         */
        private final Navigator<NavDestination> mDestNavigator = new Navigator<NavDestination>() {
            @NonNull
            @Override
            public NavDestination createDestination() {
                return new NavDestination("permissive");
            }

            @Nullable
            @Override
            public NavDestination navigate(@NonNull NavDestination destination,
                                           @Nullable Bundle args, @Nullable NavOptions navOptions,
                                           @Nullable Extras navigatorExtras) {
                throw new IllegalStateException("navigate is not supported");
            }

            @Override
            public boolean popBackStack() {
                throw new IllegalStateException("popBackStack is not supported");
            }
        };

        PermissiveNavigatorProvider() {
            addNavigator(new NavGraphNavigator(this));
        }

        @NonNull
        @Override
        public Navigator<? extends NavDestination> getNavigator(@NonNull String name) {
            try {
                return super.getNavigator(name);
            } catch (IllegalStateException e) {
                return mDestNavigator;
            }
        }
    }