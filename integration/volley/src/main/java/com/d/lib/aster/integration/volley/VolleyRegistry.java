package com.d.lib.aster.integration.volley;

import android.support.annotation.NonNull;

import com.android.volley.toolbox.HurlStack;
import com.d.lib.aster.base.AsterModule;

public class VolleyRegistry extends AsterModule.Registry {
    private HurlStack httpStack;

    public static VolleyRegistry factory() {
        return new VolleyRegistry();
    }

    public static VolleyRegistry factory(HurlStack httpStack) {
        return new VolleyRegistry().setHttpStack(httpStack);
    }

    private VolleyRegistry() {
        super();
    }

    public VolleyRegistry(@NonNull AsterModule module) {
        super(module);
    }

    public HurlStack getHttpStack() {
        return httpStack;
    }

    public VolleyRegistry setHttpStack(HurlStack httpStack) {
        this.httpStack = httpStack;
        return this;
    }
}
