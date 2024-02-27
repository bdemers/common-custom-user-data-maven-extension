package com.gradle.ccud.adapters.develocity.enterprise;

import com.gradle.ccud.adapters.BuildResultAdapter;
import com.gradle.ccud.adapters.BuildScanApiAdapter;
import com.gradle.ccud.adapters.BuildScanCaptureAdapter;
import com.gradle.ccud.adapters.BuildScanDataObfuscationAdapter;
import com.gradle.ccud.adapters.PublishedBuildScanAdapter;
import com.gradle.develocity.agent.maven.api.scan.BuildScanApi;

import java.net.URI;
import java.util.function.Consumer;

class DevelocityBuildScanApiAdapter implements BuildScanApiAdapter {

    private final BuildScanApi buildScan;

    DevelocityBuildScanApiAdapter(BuildScanApi buildScan) {
        this.buildScan = buildScan;
    }

    @Override
    public void tag(String tag) {
        buildScan.tag(tag);
    }

    @Override
    public void value(String name, String value) {
        buildScan.value(name, value);
    }

    @Override
    public void link(String name, String url) {
        buildScan.link(name, url);
    }

    @Override
    public void background(Consumer<? super BuildScanApiAdapter> action) {
        buildScan.background(__ -> action.accept(this));
    }

    @Override
    public void buildFinished(Consumer<? super BuildResultAdapter> action) {
        buildScan.buildFinished(result -> action.accept(BuildResultAdapter.from(result)));
    }

    @Override
    public void buildScanPublished(Consumer<? super PublishedBuildScanAdapter> action) {
        buildScan.buildScanPublished(scan -> action.accept(PublishedBuildScanAdapter.from(scan)));
    }

    @Override
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        buildScan.setTermsOfServiceUrl(termsOfServiceUrl);
    }

    @Override
    public String getTermsOfServiceUrl() {
        return buildScan.getTermsOfServiceUrl();
    }

    @Override
    public void setTermsOfServiceAgree(String agree) {
        buildScan.setTermsOfServiceAgree(agree);
    }

    @Override
    public String getTermsOfServiceAgree() {
        return buildScan.getTermsOfServiceAgree();
    }

    @Override
    public void setServer(URI url) {
        buildScan.setServer(url);
    }

    @Override
    public String getServer() {
        return buildScan.getServer();
    }

    @Override
    public void setAllowUntrustedServer(boolean allow) {
        buildScan.setAllowUntrustedServer(allow);
    }

    @Override
    public boolean getAllowUntrustedServer() {
        return buildScan.getAllowUntrustedServer();
    }

    @Override
    public void publishAlways() {
        buildScan.publishing(p -> p.onlyIf(ctx -> true));
    }

    @Override
    public void publishAlwaysIf(boolean condition) {
        buildScan.publishing(p -> p.onlyIf(ctx -> condition));
    }

    @Override
    public void publishOnFailure() {
        buildScan.publishing(p -> p.onlyIf(ctx -> !ctx.getBuildResult().getFailures().isEmpty()));
    }

    @Override
    public void publishOnFailureIf(boolean condition) {
        buildScan.publishing(p -> p.onlyIf(ctx -> !ctx.getBuildResult().getFailures().isEmpty() && condition));
    }

    @Override
    public void publishOnDemand() {
        // on-demand publication in the new API is controlled only via -Dscan property
    }

    @Override
    public void setUploadInBackground(boolean uploadInBackground) {
        buildScan.setUploadInBackground(uploadInBackground);
    }

    @Override
    public boolean isUploadInBackground() {
        return buildScan.isUploadInBackground();
    }

    @Override
    public void executeOnce(String identifier, Consumer<? super BuildScanApiAdapter> action) {
        buildScan.executeOnce(identifier, __ -> action.accept(this));
    }

    @Override
    public BuildScanDataObfuscationAdapter getObfuscation() {
        return null;
    }

    @Override
    public void obfuscation(Consumer<? super BuildScanDataObfuscationAdapter> action) {

    }

    @Override
    public BuildScanCaptureAdapter getCapture() {
        return null;
    }

    @Override
    public void capture(Consumer<? super BuildScanCaptureAdapter> action) {

    }
}
