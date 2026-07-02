import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TripListPage } from './trip-list.page';

describe('TripListPage', () => {
  let component: TripListPage;
  let fixture: ComponentFixture<TripListPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TripListPage],
    }).compileComponents();

    fixture = TestBed.createComponent(TripListPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
