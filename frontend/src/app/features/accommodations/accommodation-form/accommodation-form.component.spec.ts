import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccommodationFormComponent } from './accommodation-form.component';

describe('AccommodationFormComponent', () => {
  let component: AccommodationFormComponent;
  let fixture: ComponentFixture<AccommodationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccommodationFormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AccommodationFormComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
