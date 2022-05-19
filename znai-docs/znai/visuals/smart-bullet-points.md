# Types

Bullet points are an essential tool in the presentation of information.
Frequent use of bullet points however may make documentation and presentation boring.

You can change the way bullet points are rendered by using the `meta` plugin.

    :include-meta: {bulletListType: "BulletsListType"}
    
    * Testing
    * Design
    * Code Review
    * Documentation
    
# Left Right Timeline

Use `LeftRightTimeLine` to outline your flow. This also splits related things by moving odd points to the left and even 
to the right.  

    :include-meta: {bulletListType: "LeftRightTimeLine"}
    
    * Testing
    * Design
    * Code Review
    * Documentation

:include-meta: {bulletListType: "LeftRightTimeLine"}

* Testing
* Design
* Code Review
* Documentation

    
# Venn

Use `Venn` to display overlapping relationships. 

:include-meta: {bulletListType: "Venn"}

* Velocity
* Volume
* Variety

# Steps

Use `Steps` to outline a procedure.
    
:include-meta: {bulletListType: "Steps"}

* Install IDEA
* Learn Java
* Learn Machine Learning

Use `differentColors: true` to use distinct colors for each step

    :include-meta: {bulletListType: "Steps", differentColors: true}

:include-meta: {bulletListType: "Steps", differentColors: true}

* Install IDEA
* Learn Java
* Learn Machine Learning

Use `align: "left"` to align steps to the left

    :include-meta: {bulletListType: "Steps", differentColors: true, align: "left"}

:include-meta: {bulletListType: "Steps", differentColors: true, align: "left"}

* Install IDEA
* Learn Java
* Learn Machine Learning


# Presentation Only

If you want to change bullets type only for presentation use: 

    :include-meta: {presentationBulletListType: "BulletsListType"}


# Horizontal Stripes

The following bullet points will be rendered as horizontal striped, but only in presentation mode. 

    :include-meta: {presentationBulletListType: "HorizontalStripes"}

:include-meta: {presentationBulletListType: "HorizontalStripes"}

* Lessons Learned
* Future Plans
* Backup Strategy