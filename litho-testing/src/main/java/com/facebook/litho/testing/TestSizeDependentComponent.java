/*
 * Copyright 2014-present Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.litho.testing;

import android.support.v4.util.Pools;
import com.facebook.litho.Column;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.yoga.YogaEdge;

public class TestSizeDependentComponent extends Component {

  private static final Pools.SynchronizedPool<Builder> sBuilderPool =
      new Pools.SynchronizedPool<>(2);

  private TestSizeDependentComponent() {
    super("TestSizeDependentComponent");
  }

  @Override
  protected Component onCreateLayoutWithSizeSpec(
      ComponentContext c, int widthSpec, int heightSpec) {

    final Component.Builder builder1 =
        TestDrawableComponent.create(c, false, true, true, false, false)
            .flexShrink(0)
            .backgroundColor(0xFFFF0000);
    final Component.Builder builder2 = TestViewComponent.create(c, false, true, true, false)
        .flexShrink(0)
        .marginPx(YogaEdge.ALL, 3);

    if (hasFixedSizes) {
      builder1
          .widthPx(50)
          .heightPx(50);
      builder2
          .heightPx(20);
    }

    if (isDelegate) {
      return builder1.build();
    }

    return Column.create(c)
        .paddingPx(YogaEdge.ALL, 5)
        .child(builder1)
        .child(builder2)
        .build();
  }

  @Override
  protected boolean canMeasure() {
    return true;
  }

  @Override
  public MountType getMountType() {
    return MountType.NONE;
  }

  public static Builder create(ComponentContext context) {
    Builder builder = sBuilderPool.acquire();
    if (builder == null) {
      builder = new Builder();
    }
    builder.init(context, new TestSizeDependentComponent());

    return builder;
  }

  boolean hasFixedSizes;
  boolean isDelegate;

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TestSizeDependentComponent state = (TestSizeDependentComponent) other;
    if (this.getId() == state.getId()) {
      return true;
    }

    return true;
  }

  public static class Builder extends com.facebook.litho.Component.Builder<Builder> {

    TestSizeDependentComponent mTestSizeDependentComponent;

    private void init(ComponentContext context, TestSizeDependentComponent state) {
      super.init(context, 0, 0, state);
      mTestSizeDependentComponent = state;
    }

    public Builder setFixSizes(boolean hasFixSizes) {
      mTestSizeDependentComponent.hasFixedSizes = hasFixSizes;
      return this;
    }

    public Builder setDelegate(boolean isDelegate) {
      mTestSizeDependentComponent.isDelegate = isDelegate;
      return this;
    }

    @Override
    public Builder getThis() {
      return this;
    }

    @Override
    public Component build() {
      TestSizeDependentComponent testSizeDependentComponent = mTestSizeDependentComponent;
      release();
      return testSizeDependentComponent;
    }

    @Override
    protected void release() {
      super.release();
      mTestSizeDependentComponent = null;
      sBuilderPool.release(this);
    }
  }
}
